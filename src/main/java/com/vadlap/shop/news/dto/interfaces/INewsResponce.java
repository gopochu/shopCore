package com.vadlap.shop.news.dto.interfaces;

import java.time.Instant;

public interface INewsResponce {
    Long getId();
    String getTitle();
    String getContent();
    String getCoverImageUrl();
    String getAuthorId();
    boolean isPublished();
    Instant getCreatedAt();
    Instant getUpdatedAt();
}
