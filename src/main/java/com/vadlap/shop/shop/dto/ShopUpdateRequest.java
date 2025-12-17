package com.vadlap.shop.shop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ShopUpdateRequest {
    @NotBlank(message = "Название магазина не может быть пустым")
    @Size(max = 255, message = "Название не может превышать 255 символов")
    private String name;

    private String description;
}
