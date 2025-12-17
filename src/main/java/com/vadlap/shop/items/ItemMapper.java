package com.vadlap.shop.items;

import com.vadlap.shop.items.dto.ItemCreateRequest;
import com.vadlap.shop.items.dto.ItemResponse;
import com.vadlap.shop.items.entity.ItemEntity;

public class ItemMapper {

    public static ItemEntity toEntity(ItemCreateRequest dto) {
        ItemEntity entity = new ItemEntity();
        entity.setName(dto.getName());
        entity.setPrice(dto.getPrice());
        entity.setItemCounter(dto.getItemCounter());
        entity.setImageUrl(dto.getImageUrl());
        entity.setDescription(dto.getDescription());
        entity.setBrand(dto.getBrand());
        entity.setWeight(dto.getWeight());
        return entity;
    }

    public static ItemResponse toResponse(ItemEntity entity) {
        ItemResponse response = new ItemResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setPrice(entity.getPrice());
        response.setItemCounter(entity.getItemCounter());
        response.setImageUrl(entity.getImageUrl());
        response.setDescription(entity.getDescription());
        response.setBrand(entity.getBrand());
        response.setWeight(entity.getWeight());
        return response;
    }
}