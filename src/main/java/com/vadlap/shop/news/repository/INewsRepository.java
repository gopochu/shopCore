package com.vadlap.shop.news.repository;
import com.vadlap.shop.news.entity.NewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface INewsRepository extends JpaRepository<NewsEntity, Long> {
}
