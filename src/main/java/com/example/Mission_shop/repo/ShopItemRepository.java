package com.example.Mission_shop.repo;

import com.example.Mission_shop.entity.Shop;
import com.example.Mission_shop.entity.ShopItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShopItemRepository extends JpaRepository<ShopItem, Long> {
    Optional<ShopItem> findByShopAndName(Shop shop, String name);
}
