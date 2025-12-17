package com.vadlap.shop.shop;

import com.vadlap.shop.shop.dto.ShopCreateRequest;
import com.vadlap.shop.shop.dto.ShopResponse;
import com.vadlap.shop.shop.entity.ShopEntity;

public class ShopMapper {
    public static ShopEntity toEntity(ShopCreateRequest dto) {
        ShopEntity entity = new ShopEntity();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        return entity;
    }

    public static ShopResponse toResponse(ShopEntity entity) {
        ShopResponse response = new ShopResponse();
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        return response;
    }
}
