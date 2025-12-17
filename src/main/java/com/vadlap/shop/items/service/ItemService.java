package com.vadlap.shop.items.service;

import com.vadlap.shop.exceptions.ItemNotFoundException;
import com.vadlap.shop.exceptions.PermissionDeniedException;
import com.vadlap.shop.exceptions.ShopAccessDeniedException;
import com.vadlap.shop.exceptions.ShopNotFoundException;
import com.vadlap.shop.items.ItemMapper;
import com.vadlap.shop.items.dto.ItemCreateRequest;
import com.vadlap.shop.items.dto.ItemResponse;
import com.vadlap.shop.items.dto.ItemUpdateRequest;
import com.vadlap.shop.items.entity.ItemEntity;
import com.vadlap.shop.items.repository.IItemRepository;
import com.vadlap.shop.roles.Role;
import com.vadlap.shop.roles.user.User;
import com.vadlap.shop.shop.entity.ShopEntity;
import com.vadlap.shop.shop.repository.IShopRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemService {

    private final IItemRepository itemRepository;
    private final IShopRepository shopRepository;

    private static final long MIN_ARTICLE_NUMBER = 100000L;

    // Конструктор, принимающий обе зависимости
    public ItemService(IItemRepository itemRepository, IShopRepository shopRepository) {
        this.itemRepository = itemRepository;
        this.shopRepository = shopRepository;
    }

    private Long generateArticle() {
        // ... (Ваша логика генерации артикула остается прежней)
        Optional<Long> maxArticleOpt = itemRepository.findMaxArticleNumber();
        // ... (Код генерации)

        Long nextArticle;
        if(maxArticleOpt.isPresent()) {
            Long maxArticle = maxArticleOpt.get();
            nextArticle = Math.max(maxArticle + 1, MIN_ARTICLE_NUMBER);
        } else {
            nextArticle = MIN_ARTICLE_NUMBER;
        }
        return nextArticle;
    }

    @Transactional
    public ItemResponse createItem(ItemCreateRequest request, User currentUser) {

        // --- 1. ПРОВЕРКА МАГАЗИНА И ПРАВ ВЛАДЕЛЬЦА ---
        Long shopId = request.getShopId();

        // Ищем магазин по ID
        ShopEntity shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ShopNotFoundException("Магазин с ID " + shopId + " не найден."));

        // Проверяем, что текущий пользователь является автором этого магазина
        if (!Objects.equals(shop.getAuthorId(), currentUser.getId())) {
            throw new ShopAccessDeniedException("Вы не являетесь владельцем магазина с ID " + shopId);
        }

        // --- 2. СОЗДАНИЕ ТОВАРА ---
        ItemEntity entity = ItemMapper.toEntity(request);

        // Присваиваем ID магазина и ID автора
        entity.setAuthorId(currentUser.getId());
        entity.setShopId(shopId); // <-- Используем shopId из запроса/магазина

        // Генерируем артикул
        Long newArticle = this.generateArticle();
        entity.setArticle(newArticle);

        ItemEntity savedEntity = itemRepository.save(entity);
        return ItemMapper.toResponse(savedEntity);
    }

    // Получение товара по ID
    public ItemResponse getItemById(Long id) {
        ItemEntity entity = itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Товар не найден")); // <-- Использование ItemNotFoundException
        return ItemMapper.toResponse(entity);
    }

    // Получение всех товаров (оставляем без изменений)
    public List<ItemResponse> getAllItems() {
        return itemRepository.findAll().stream()
                .map(ItemMapper::toResponse)
                .collect(Collectors.toList());
    }

    // --- ОБНОВЛЕННЫЕ deleteItem и changeItem ---

    public void deleteItem(Long id, User currentUser) { // Убираем 'throws Exception'
        ItemEntity entity = itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Товар не найден."));

        ShopEntity shop = shopRepository.findById(entity.getShopId())
                .orElseThrow(() -> new ShopNotFoundException("Связанный магазин не найден."));

        // Проверка прав
        if (currentUser.getRole() == Role.ADMIN) {
            // ADMIN может удалить любой товар
        } else if (currentUser.getRole() == Role.EDITOR) {
            // EDITOR может удалить товар ТОЛЬКО если он его автор ИЛИ владелец магазина
            if (!Objects.equals(entity.getAuthorId(), currentUser.getId()) &&
                    !Objects.equals(shop.getAuthorId(), currentUser.getId())) { // Добавляем проверку владельца магазина
                throw new PermissionDeniedException("Вы не можете удалить этот товар, так как не являетесь его автором или владельцем магазина.");
            }
        } else {
            throw new PermissionDeniedException("У вас нет прав на удаление товаров.");
        }

        itemRepository.delete(entity);
    }

    public ItemResponse changeItem(Long id, ItemUpdateRequest request, User currentUser) { // Убираем 'throws Exception'
        ItemEntity entity = itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Товар с ID " + id + " не найден."));

        // Получаем магазин для дополнительной проверки
        ShopEntity shop = shopRepository.findById(entity.getShopId())
                .orElseThrow(() -> new ShopNotFoundException("Связанный магазин не найден."));

        // Проверка прав
        if (currentUser.getRole() == Role.ADMIN) {
        } else if (currentUser.getRole() == Role.EDITOR) {
            // EDITOR может редактировать товар ТОЛЬКО если он его автор ИЛИ владелец магазина
            if (!Objects.equals(entity.getAuthorId(), currentUser.getId()) &&
                    !Objects.equals(shop.getAuthorId(), currentUser.getId())) {
                throw new PermissionDeniedException("Вы не можете редактировать этот товар, так как не являетесь его автором или владельцем магазина.");
            }
        } else {
            throw new PermissionDeniedException("У вас нет прав на редактирование.");
        }

        if (request.getName() != null) {
            entity.setName(request.getName());
        }

        ItemEntity updatedEntity = itemRepository.save(entity);
        return ItemMapper.toResponse(updatedEntity);
    }
}