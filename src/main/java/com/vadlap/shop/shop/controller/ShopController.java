package com.vadlap.shop.shop.controller;

import com.vadlap.shop.roles.user.User;
import com.vadlap.shop.shop.dto.ShopCreateRequest;
import com.vadlap.shop.shop.dto.ShopResponse;
import com.vadlap.shop.shop.dto.ShopUpdateRequest;
import com.vadlap.shop.shop.service.ShopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Shops", description = "Управление магазинами")
@RestController
@RequestMapping("/api/v1/shops")
public class ShopController {

    private final ShopService shopService;
    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @Operation(summary = "Создать новый магазин")
    // Разрешаем создание только авторизованным пользователям (например, EDITOR)
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<ShopResponse> createShop(
            @Valid @RequestBody ShopCreateRequest request,
            @AuthenticationPrincipal User currentUser) {

        ShopResponse response = shopService.createShop(request, currentUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Получить список всех магазинов")
    @GetMapping
    public ResponseEntity<List<ShopResponse>> getAllShops() {
        List<ShopResponse> response = shopService.getAllShops();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Удалить магазин по ID")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EDITOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShop(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) throws Exception {

        shopService.deleteShop(id, currentUser);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @Operation(summary = "Изменить магазин по ID")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EDITOR')")
    @PatchMapping("/{id}")
    public ResponseEntity<ShopResponse> changeShop(
            @PathVariable Long id,
            @Valid @RequestBody ShopUpdateRequest request,
            @AuthenticationPrincipal User currentUser) throws Exception {

        ShopResponse response = shopService.changeShop(id, request, currentUser);
        return ResponseEntity.ok(response);
    }
}