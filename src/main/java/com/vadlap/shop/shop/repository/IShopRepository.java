package com.vadlap.shop.shop.repository;

import com.vadlap.shop.shop.entity.ShopEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IShopRepository extends JpaRepository<ShopEntity, Long> {
}
