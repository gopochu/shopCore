package com.vadlap.shop.news;

import com.vadlap.shop.news.dto.NewsResponse;
import com.vadlap.shop.news.dto.interfaces.ICreateNews;
import com.vadlap.shop.news.dto.interfaces.IUpdateNews;
import com.vadlap.shop.news.entity.NewsEntity;

import java.time.Instant;

public class NewsMapper {
    public static NewsEntity createNewsToNewsEntity(ICreateNews dto, String authorId) {
        NewsEntity entity = new NewsEntity();
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setImageUrl(dto.getImageUrl());
        entity.setAuthorId(authorId);
        entity.setPublished(false);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
        return entity;
    }

    public static void updateNewsToNewsEntity(IUpdateNews dto, NewsEntity entity) {
        if(dto.getTitle() != null) entity.setTitle(dto.getTitle());
        if(dto.getContent() != null) entity.setContent(dto.getContent());
        if(dto.getCoverImageUrl() != null) entity.setImageUrl(dto.getCoverImageUrl());
        if(dto.getIsPublished() != null) entity.setPublished(dto.getIsPublished());
        entity.setUpdatedAt(Instant.now());
    }

    public static NewsResponse newsEntityToNewsResponse(NewsEntity entity) {
        NewsResponse dto = new NewsResponse();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setCoverImageUrl(entity.getImageUrl());
        dto.setAuthorId(entity.getAuthorId());
        dto.setPublished(entity.isPublished());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
