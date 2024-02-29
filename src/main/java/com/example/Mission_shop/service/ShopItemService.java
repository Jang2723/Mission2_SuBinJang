package com.example.Mission_shop.service;

import com.example.Mission_shop.dto.ItemDto;
import com.example.Mission_shop.dto.ShopItemDto;
import com.example.Mission_shop.entity.Item;
import com.example.Mission_shop.entity.Shop;
import com.example.Mission_shop.entity.ShopItem;
import com.example.Mission_shop.repo.ShopItemRepository;
import com.example.Mission_shop.repo.ShopRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ShopItemService {
    private final ShopItemRepository shopItemRepository;
    private final ShopRepository shopRepository;

    public String registerShopItem (ShopItemDto shopItemDto, String username) {
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

    // 상품 정보 수정
    @Transactional
    public String updateShopItem(ShopItemDto shopItemDto, String username, String name) {
        // 사용자 이름으로 상점을 찾음
        Optional<Shop> optionalShop = shopRepository.findByUserUsername(username);

        if (optionalShop.isPresent()) {
            Shop shop = optionalShop.get();

            // 해당 상점의 아이템을 찾음
            Optional<ShopItem> optionalShopItem = shopItemRepository.findByShopAndName(shop, name);

            if (optionalShopItem.isPresent()) {
                ShopItem shopItem = optionalShopItem.get();

                // 새로운 정보로 엔티티 업데이트
                shopItem.setName(shopItemDto.getName());
                shopItem.setDescription(shopItemDto.getDescription());
                shopItem.setPrice(shopItemDto.getPrice());
                shopItem.setMainCategory(shopItemDto.getMainCategory());
                shopItem.setSubCategory(shopItemDto.getSubCategory());
                shopItem.setStock(shopItemDto.getStock());

                // 엔티티 저장
                shopItemRepository.save(shopItem);

                return "상품 정보가 업데이트되었습니다.";
            } else {
                return "해당 상품을 찾을 수 없습니다.";
            }
        } else {
            return "사용자의 상점을 찾을 수 없습니다.";
        }
    }

    // 쇼핑몰 상품 삭제
    public String deleteShopItem (String name, String username) {
        // name은 shptiem의 이름, username은 shop의 소유자
        // 사용자 이름을 기반으로 해당 사용자의 쇼핑몰을 찾음
        Optional<Shop> optionalShop = shopRepository.findByUserUsername(username);

        if (optionalShop.isPresent()) {
            // 쇼핑몰을 찾음
            Shop shop = optionalShop.get();
            // 쇼핑몰에 속한 상품을 찾아서 삭제
            Optional<ShopItem> optionalShopItem = shopItemRepository.findByShopAndName(shop, name);
            if (optionalShopItem.isPresent()) {
                // 상품을 찾고 삭제
                ShopItem shopItem = optionalShopItem.get();
                shopItemRepository.delete(shopItem);
                return "쇼핑몰 상품 삭제 완료";
            } else {
                return "해당 이름의 상품을 찾을 수 없습니다.";
            }
        } else {
            return "해당 사용자의 쇼핑몰을 찾을 수 없습니다.";
        }
    }

    // 상품검색
    // 이름,가격 범위를 기준으로 상품 검색, 조회되는 상품이 등록된 쇼핑몰에 대한 정보가 함께 제공
    public List<Object[]> searchShopItem(String name, Integer minPrice, Integer maxPrice) {
        List<ShopItem> foundItems;

        if (name != null && minPrice != null && maxPrice != null) {
            // 이름과 가격 범위로 상품 검색
            foundItems = shopItemRepository.findByNameContainingAndPriceBetween(name, minPrice, maxPrice);
        } else if (name != null) {
            // 이름으로만 상품 검색
            foundItems = shopItemRepository.findByNameContaining(name);
        } else if (minPrice != null && maxPrice != null) {
            // 가격 범위로 상품 검색
            foundItems = shopItemRepository.findByPriceBetween(minPrice, maxPrice);
        } else {
            // 모든 상품 검색
            foundItems = shopItemRepository.findAll();
        }

        // 검색된 상품 및 쇼핑몰 정보를 함께 반환
        List<Object[]> result = new ArrayList<>();
        for (ShopItem item : foundItems) {
            Object[] itemWithShopInfo = new Object[6];
            itemWithShopInfo[0] = "상품 이름: " + item.getName();
            itemWithShopInfo[1] = "상품 가격: " + item.getPrice();
            itemWithShopInfo[2] = "상품 소개: " + item.getDescription();
            itemWithShopInfo[3] = "쇼핑몰 이름: " + item.getShop().getName();
            itemWithShopInfo[4] = "쇼핑몰 소개: " + item.getShop().getIntroduction();
            itemWithShopInfo[5] = "쇼핑몰 분류: " + item.getShop().getCategory();
            result.add(itemWithShopInfo);
        }
        return result;
    }

}
