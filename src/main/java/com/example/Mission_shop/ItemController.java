package com.example.Mission_shop;

import com.example.Mission_shop.dto.ItemDto;
import com.example.Mission_shop.entity.Item;
import com.example.Mission_shop.repo.ItemRepository;
import com.example.Mission_shop.repo.UserRepository;
import com.example.Mission_shop.service.ItemService;
import com.example.Mission_shop.service.JpaUserDetailsManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemService itemService;


    @PostMapping("/register")
    public String registerItem(@RequestBody ItemDto itemDto) {
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // 이제 userDetails를 사용하여 사용자 정보를 가져올 수 있습니다.
            String username = userDetails.getUsername();

            // 사용자의 ROLE이 USER인지 확인
            boolean isUser = userDetails.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"));

            if (!isUser) {
                return "일반 사용자만 중고거래 등록이 가능합니다.";
            }

            return itemService.registerItem(itemDto, username);
        } else {
            // username과 password가 일치하지 않을 경우 처리
            return "Authentication failed. Invalid username or password.";
        }
    }

    // 등록된 물품 정보 보기
    @GetMapping("/itemAllList")
    public List<ItemDto> ItemAllList() {
        return itemService.itemAllList();

    }


}

