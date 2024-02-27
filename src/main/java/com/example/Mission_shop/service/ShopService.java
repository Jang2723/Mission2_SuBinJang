package com.example.Mission_shop.service;

import com.example.Mission_shop.dto.ShopDto;
import com.example.Mission_shop.entity.Shop;
import com.example.Mission_shop.repo.ShopRepository;
import com.example.Mission_shop.repo.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

}
