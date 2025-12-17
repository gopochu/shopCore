package com.vadlap.shop.items.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemUpdateRequest {
    private String name;

    @DecimalMin(value = "0.01", message = "Цена должна быть положительной")
    private BigDecimal price;

    @Min(value = 0, message = "Количество не может быть отрицательным")
    private short itemCounter;

    private String article;
    private String imageUrl;

    private String description;

    private String brand;
    private int weight;
}
