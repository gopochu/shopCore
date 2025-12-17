package com.vadlap.shop.news.service;

import com.vadlap.shop.news.dto.NewsResponse;
import com.vadlap.shop.news.dto.interfaces.ICreateNews;
import com.vadlap.shop.roles.user.User;
import java.util.List;

public interface INewsService {
    NewsResponse createNews(ICreateNews createNewsDto, User currentUser, String newsLocation) throws Exception;

    List<NewsResponse> findAllPublished();
    void deleteNews(Long newsId, User currentUser) throws Exception;
}