package com.example.Mission_shop.repo;

import com.example.Mission_shop.entity.OrderShopItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderShopItemRepository extends JpaRepository<OrderShopItem, Long> {
    List<OrderShopItem> findAllByOrderByDateTimeDesc();

    List<OrderShopItem> findByUserUsername(String username);

    List<OrderShopItem> findByShopIdAndStatus(Long id, String 구매_요청);
}
