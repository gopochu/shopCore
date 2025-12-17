package com.vadlap.shop.exceptions;

public class ShopAccessDeniedException extends RuntimeException {
    public ShopAccessDeniedException(String message) {
        super(message);
    }
}
