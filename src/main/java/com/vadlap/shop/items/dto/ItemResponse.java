package com.vadlap.shop.items.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ItemResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private short itemCounter;
    private String article;
    private String imageUrl;
    private String description;
    private String brand;
    private int weight;
}