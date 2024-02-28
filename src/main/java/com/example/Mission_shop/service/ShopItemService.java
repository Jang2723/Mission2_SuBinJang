package com.example.Mission_shop.service;

import com.example.Mission_shop.dto.ShopItemDto;
import com.example.Mission_shop.entity.Shop;
import com.example.Mission_shop.entity.ShopItem;
import com.example.Mission_shop.repo.ShopItemRepository;
import com.example.Mission_shop.repo.ShopRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ShopItemService {
    private final ShopItemRepository shopItemRepository;
    private final ShopRepository shopRepository;

    public String registerSItem (ShopItemDto shopItemDto, String username) {
        // 사용자 이름으로 상점을 찾음
        Optional<Shop> optionalShop = shopRepository.findByUserUsername(username);

        if (optionalShop.isPresent()) {
            Shop shop = optionalShop.get();

            // 새로운 상품 생성
            ShopItem shopItem = ShopItem.builder()
                    .name(shopItemDto.getName())
                    .description(shopItemDto.getDescription())
                    .price(shopItemDto.getPrice())
                    .mainCategory(shopItemDto.getMainCategory())
                    .subCategory(shopItemDto.getSubCategory())
                    .stock(shopItemDto.getStock())
                    .shop(shop) // 상점 정보 설정
                    .build();

            // 상점 저장 (이 때 상품도 함께 저장됨)
            shopItemRepository.save(shopItem);

            return "상품 등록 완료";
        } else {
            return "사용자의 상점을 찾을 수 없습니다.";
        }
    }
}
