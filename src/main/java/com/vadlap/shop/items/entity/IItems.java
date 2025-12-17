package com.vadlap.shop.items.entity;

import java.math.BigDecimal;

public interface IItems {
    Long getId();
    String getName();
    BigDecimal getPrice();
    short getItemCounter();
    Long getArticle();
    String getImageUrl();
    String getDescription();
    String getBrand();
    int getWeight();
}
