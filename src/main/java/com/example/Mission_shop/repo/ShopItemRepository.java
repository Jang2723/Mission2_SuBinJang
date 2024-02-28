package com.example.Mission_shop.repo;

import com.example.Mission_shop.entity.ShopItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopItemRepository extends JpaRepository<ShopItem, Long> {
}
