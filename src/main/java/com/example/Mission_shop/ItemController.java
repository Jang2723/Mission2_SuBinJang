package com.example.Mission_shop;

import com.example.Mission_shop.dto.ItemDto;
import com.example.Mission_shop.entity.Item;
import com.example.Mission_shop.repo.ItemRepository;
import com.example.Mission_shop.repo.UserRepository;
import com.example.Mission_shop.service.JpaUserDetailsManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("items")
@RequiredArgsConstructor
public class ItemController {
//    private final ItemService itemService;
    private final PasswordEncoder passwordEncoder;
    private final JpaUserDetailsManager userDetailsManager; // JpaUserDetailsManager로 수정
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;


    @PostMapping("/register")
    public String registerItem(@RequestBody ItemDto itemDto/*,
                               @RequestParam("username") String username,
                               @RequestParam("password") String password)*/) {
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // 이제 userDetails를 사용하여 사용자 정보를 가져올 수 있습니다.
            String username = userDetails.getUsername();

            // 아이템 등록 처리
            Item item = new Item();
            item.setTitle(itemDto.getTitle());
            item.setDescription(itemDto.getDescription());
            item.setMinimumPrice(itemDto.getMinimumPrice());
            item.setStatus("판매중"); // 처음 등록할 때 "판매중" 상태로 설정
            item.setUser(userRepository.findIdByUsername(username).orElse(null)); // 현재 사용자의 아이디 설정

            // 아이템 저장
            itemRepository.save(item);

            return "Item registered successfully";
        } else {
            // username과 password가 일치하지 않을 경우 처리
            return "Authentication failed. Invalid username or password.";
        }
    }


}

