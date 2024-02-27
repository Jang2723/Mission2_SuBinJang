package com.example.Mission_shop.service;

import com.example.Mission_shop.dto.ShopDto;
import com.example.Mission_shop.entity.Shop;
import com.example.Mission_shop.repo.ShopRepository;
import com.example.Mission_shop.repo.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ShopService {
    private final UserRepository userRepository;
    private final ShopRepository shopRepository;

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

    public String shopOpenApply (String username) {
        // username을 사용하여 사용자의 쇼핑몰을 찾음
        Optional<Shop> optionalShop = shopRepository.findByUserUsername(username);

        if (optionalShop.isPresent()) {
            Shop shop = optionalShop.get();

            // name, introduction, category가 모두 null이 아니고 비어있지 않은 경우에만 개설 신청 상태로 변경
            if (shop.getName() != null && !shop.getName() .isEmpty()
                && shop.getIntroduction() != null && !shop.getIntroduction().isEmpty()
                && shop.getCategory() != null && !shop.getCategory().isEmpty()) {

                shop.setStatus("개설 신청");
                shopRepository.save(shop);
                return "쇼핑몰 개설 신청이 완료되었습니다.";
            } else {
                return "Name, introduction, category should not be null or empty.";
            }
        } else {
            return "Shop not found for username: " + username;
        }
    }

    // 관리자가 개설신청 쇼핑몰 목록 읽기
    public List<ShopDto> applyRead() {
        // 쇼핑몰 개설 신청 목록 가져와서 리스트로 저장
        String status = "개설 신청";
        List<Shop> shopList = shopRepository.findByStatus(status);

        // 각 쇼핑몰을 ShopDto로 변환하여 저장할 리스트 생성
        List<ShopDto> shopDtoList = new ArrayList<>();

        // 각 쇼핑몰을 ShopDto로 변환하여 리스트에 추가
        for (Shop shop : shopList) {
            shopDtoList.add(ShopDto.fromEntity(shop));
        }

        return shopDtoList;
    }
}
