package com.vadlap.shop.items.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemCreateRequest {

    @NotBlank(message = "Название не может быть пустым")
    private String name;

    @NotNull(message = "Цена обязательна")
    @DecimalMin(value = "0.01", message = "Цена должна быть положительной")
    private BigDecimal price;

    @Min(value = 0, message = "Количество не может быть отрицательным")
    private short itemCounter;

    private String article;
    private String imageUrl;

    @NotNull(message = "ID магазина должен быть указан")
    private Long shopId;

    @NotBlank(message = "Описание обязательно")
    private String description;

    private String brand;
    private int weight;
}