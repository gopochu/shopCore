package com.vadlap.shop.items.repository;

import com.vadlap.shop.items.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IItemRepository extends JpaRepository<ItemEntity, Long > {
    @Query("SELECT MAX(e.article) FROM ItemEntity e")
    Optional<Long> findMaxArticleNumber();

    boolean existsByArticle(Long article);
}
