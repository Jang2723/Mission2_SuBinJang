package com.example.Mission_shop.service;

import com.example.Mission_shop.dto.ShopDto;
import com.example.Mission_shop.entity.Shop;
import com.example.Mission_shop.repo.ShopRepository;
import com.example.Mission_shop.repo.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ShopService {
    private final UserRepository userRepository;
    private final ShopRepository shopRepository;

    public String registerShop (ShopDto shopDto, String username) {
        // shop 등록 처리
        Shop shop = new Shop();
        shop.setName(shopDto.getName());
        shop.setIntroduction(shopDto.getIntroduction());
        shop.setCategory(shopDto.getCategory());
        shop.setStatus("준비중");
        shop.setUser(userRepository.findIdByUsername(username).orElse(null));

        // shop 준비 신청
        shopRepository.save(shop);

        return "Shop registered successfully";
    }

    public String updateShop (ShopDto shopDto, String username) {
        // username을 사용하여 사용자의 쇼핑몰을 찾음
        Optional<Shop> optionalShop = shopRepository.findByUserUsername(username);

        if (optionalShop.isPresent()) {
            Shop shop = optionalShop.get();

            // 쇼핑몰 정보 수정
            shop.setName(shopDto.getName());
            shop.setIntroduction(shopDto.getIntroduction());
            shop.setCategory(shopDto.getCategory());

            // 저장
            shopRepository.save(shop);

            return "Shop updated successfully";
        } else {
            return "Shop not found for username: " + username;
        }
    }
}
