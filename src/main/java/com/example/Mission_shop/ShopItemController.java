package com.example.Mission_shop;

import com.example.Mission_shop.dto.ShopItemDto;
import com.example.Mission_shop.service.ShopItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("shop/items")
@RequiredArgsConstructor
public class ShopItemController {
    private final ShopItemService shopItemService;

    // 쇼핑몰에 상품 등록
    @PostMapping("/register")
    public String registerSItem (@RequestBody ShopItemDto shopItemDto) {
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // 이제 userDetails를 사용하여 사용자 정보를 가져올 수 있습니다.
            String username = userDetails.getUsername();

            // 인증된 사용자의 정보 넘겨주기
            return shopItemService.registerSItem(shopItemDto, username);
        } else {
            // username과 password가 일치하지 않을 경우 처리
            return "Authentication failed. Invalid username or password.";
        }
    }


}
