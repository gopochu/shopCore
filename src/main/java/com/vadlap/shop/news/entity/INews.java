package com.vadlap.shop.news.entity;

import java.time.Instant;

public interface INews {
    Long getId();
    String getTitle();
    String getContent();
    String getAuthor();
    Instant getCreatedAt();
    Instant getUpdatedAt();
}
