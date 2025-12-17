package com.vadlap.shop.items.controller;

import com.vadlap.shop.items.dto.ItemCreateRequest;
import com.vadlap.shop.items.dto.ItemResponse;
import com.vadlap.shop.items.dto.ItemUpdateRequest;
import com.vadlap.shop.items.service.ItemService;
import com.vadlap.shop.roles.user.User;
import com.vadlap.shop.shop.entity.ShopEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Items", description = "Эндпоинты управления товарами")
@RestController
@RequestMapping("/api/v1/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @Operation(summary = "Создать новый товар")
    // TODO: здесь нужна авторизация (например, hasRole('ADMIN'))
    @PostMapping
    public ResponseEntity<ItemResponse> createItem(@Valid @RequestBody ItemCreateRequest request,
                                                   @AuthenticationPrincipal User currentUser) {
        // В сервис передаем только request и user. ID магазина уже внутри request.
        ItemResponse response = itemService.createItem(request, currentUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Получить товар по ID")
    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse> getItemById(@PathVariable Long id) {
        ItemResponse response = itemService.getItemById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Получить все товары")
    @GetMapping
    public ResponseEntity<List<ItemResponse>> getAllItems() {
        List<ItemResponse> response = itemService.getAllItems();
        return ResponseEntity.ok(response);
    }

    // TODO: Методы PUT/PATCH для обновления, DELETE для удаления
    @Operation(summary = "Удаление товара по ID")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EDITOR')")
    // Используем ResponseEntity<Void> для явного указания, что нет тела ответа
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {

        if (currentUser == null) {
            // Если @PreAuthorize не сработал (редкий случай), но лучше полагаться на PreAuthorize
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            itemService.deleteItem(id, currentUser);
            // Возврат 204
            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            String errorMessage = e.getMessage();

            if (errorMessage != null) {
                if (errorMessage.contains("не найден")) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
                }
                if (errorMessage.contains("нет прав") || errorMessage.contains("не можете удалить")) {
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN); // 403
                }
            }
            // В случае других ошибок, возвращаем 500
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            // Примечание: для 403, 404, 500 можно возвращать ResponseEntity<?> с телом ошибки,
            // но 204 должен быть без тела.
        }
    }

    @Operation(summary = "Изменение товара по ID")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EDITOR')")
    @PatchMapping("/{id}")
    public ResponseEntity<?> changeItem(
            @PathVariable Long id,
            @RequestBody ItemUpdateRequest request,
            @AuthenticationPrincipal User currentUser) {

        if (currentUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            ItemResponse response = itemService.changeItem(id, request, currentUser);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            String errorMessage = e.getMessage();

            if (errorMessage != null) {
                if (errorMessage.contains("не найден")) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
                }
                if (errorMessage.contains("нет прав") || errorMessage.contains("не можете редактировать")) {
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN); // 403
                }
            }
            return new ResponseEntity<>("Ошибка при обновлении товара: " + errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}