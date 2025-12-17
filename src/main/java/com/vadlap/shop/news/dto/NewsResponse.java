package com.vadlap.shop.news.dto;

import com.vadlap.shop.news.dto.interfaces.INewsResponce;
import lombok.Setter;

import java.time.Instant;

@Setter
public class NewsResponse implements INewsResponce {
    private Long id;
    private String title;
    private String content;
    private String coverImageUrl;
    private String authorId;
    private boolean published;
    private Instant createdAt;
    private Instant updatedAt;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    @Override
    public String getAuthorId() {
        return authorId;
    }

    @Override
    public boolean isPublished() {
        return published;
    }

    @Override
    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
