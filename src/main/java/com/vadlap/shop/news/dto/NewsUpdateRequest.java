package com.vadlap.shop.news.dto;

import com.vadlap.shop.news.dto.interfaces.IUpdateNews;
import lombok.Setter;

@Setter
public class NewsUpdateRequest implements IUpdateNews {

    private String title;
    private String content;
    private String imageUrl;
    private boolean isPublished;

    @Override
    public String getTitle() {
        return "";
    }

    @Override
    public String getContent() {
        return "";
    }

    @Override
    public String getCoverImageUrl() {
        return "";
    }

    @Override
    public Boolean getIsPublished() {
        return null;
    }
}
