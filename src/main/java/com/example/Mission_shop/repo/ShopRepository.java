package com.example.Mission_shop.repo;

import com.example.Mission_shop.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    Optional<Shop> findByUserUsername(String username);
}
