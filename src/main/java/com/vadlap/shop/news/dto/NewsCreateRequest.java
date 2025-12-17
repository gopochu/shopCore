package com.vadlap.shop.news.dto;

import com.vadlap.shop.news.dto.interfaces.ICreateNews;
import lombok.Setter;
@Setter
public class NewsCreateRequest implements ICreateNews {

    private String title;
    private String content;
    private String imageUrl;

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public String getImageUrl() {
        return imageUrl;
    }
}
