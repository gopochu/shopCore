package com.vadlap.shop.shop.service;

import com.vadlap.shop.items.ItemMapper;
import com.vadlap.shop.roles.Role;
import com.vadlap.shop.roles.user.User;
import com.vadlap.shop.shop.ShopMapper;
import com.vadlap.shop.shop.dto.ShopCreateRequest;
import com.vadlap.shop.shop.dto.ShopResponse;
import com.vadlap.shop.shop.dto.ShopUpdateRequest;
import com.vadlap.shop.shop.entity.ShopEntity;
import com.vadlap.shop.shop.repository.IShopRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ShopService {

    private final IShopRepository shopRepository;

    ShopService(IShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    public ShopResponse createShop(ShopCreateRequest request, User currentUser) {
        ShopEntity entity = ShopMapper.toEntity(request);
        entity.setAuthorId(currentUser.getId());

        ShopEntity savedEntity = shopRepository.save(entity);
        return ShopMapper.toResponse(savedEntity);
    }

    public List<ShopResponse> getAllShops() {
        return shopRepository.findAll().stream()
                .map(ShopMapper::toResponse)
                .collect(Collectors.toList());
    }

    public void deleteShop(Long id, User currentUser) throws Exception {
        ShopEntity entity = shopRepository.findById(id)
                .orElseThrow(() -> new Exception("Магазин не найден"));

        if(currentUser.getRole() == Role.ADMIN) {}
        else if (currentUser.getRole() == Role.EDITOR) {
            if (!Objects.equals(entity.getAuthorId(), currentUser.getId())) {
                throw new Exception("Вы не можете удалить текущий магазин");
            }
        } else {
            throw new Exception("У вас нет прав на удаление магазинов");
        }

        shopRepository.delete(entity);
    }

    public ShopResponse changeShop(Long id, ShopUpdateRequest request, User currentUser) throws Exception {
        ShopEntity entity = shopRepository.findById(id)
                .orElseThrow(() -> new Exception("Магазин с ID" + id + "не найден"));

        if (currentUser.getRole() == Role.ADMIN) {}
        else if (currentUser.getRole() == Role.EDITOR) {
            if(!Objects.equals(entity.getAuthorId(), currentUser.getId())) {
                throw new Exception("Вы не можете изменить текущий магазин");
            }
        } else {
            throw new Exception("У вас нету прав на изменение магазина");
        }

        if (request.getName() != null)
            entity.setName(request.getName());

        if (request.getDescription() != null)
            entity.setDescription(request.getDescription());

        ShopEntity updatedEntity = shopRepository.save(entity);
        return ShopMapper.toResponse(updatedEntity);
    }
}
