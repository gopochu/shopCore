package com.vadlap.shop.shop.dto;

import lombok.Data;

@Data
public class ShopResponse {

    private Long id;
    private String name;
    private String description;

    // Поле, которое может быть полезно, но не должно быть в запросе
    private Long authorId;
}