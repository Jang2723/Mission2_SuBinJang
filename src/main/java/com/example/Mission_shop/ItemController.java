package com.example.Mission_shop;

import com.example.Mission_shop.dto.ItemDto;
import com.example.Mission_shop.dto.OfferDto;
import com.example.Mission_shop.entity.Item;
import com.example.Mission_shop.repo.ItemRepository;
import com.example.Mission_shop.repo.UserRepository;
import com.example.Mission_shop.service.ItemNotFoundException;
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
    private final ItemService itemService;



    @PostMapping("/register")
    public String registerItem(@RequestBody ItemDto itemDto) {
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // 이제 userDetails를 사용하여 사용자 정보를 가져올 수 있습니다.
            String username = userDetails.getUsername();

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

    // 등록된 물품 정보 작성자 수정
    @PostMapping("/update")
    public String updateItem(@RequestBody ItemDto itemDto){
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // 이제 userDetails를 사용하여 사용자 정보를 가져올 수 있습니다.
            String username = userDetails.getUsername();

            return itemService.updateItem(itemDto, username);
        } else {
            // username과 password가 일치하지 않을 경우 처리
            return "Authentication failed. Invalid username or password.";
        }
    }

    // 등록한 물품 작성자가 삭제
    @PostMapping("/delete")
    public String deleteItem (@RequestBody ItemDto itemDto){
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // 이제 userDetails를 사용하여 사용자 정보를 가져올 수 있습니다.
            String username = userDetails.getUsername();

            return itemService.deleteItem(itemDto, username);
        } else {
            // username과 password가 일치하지 않을 경우 처리
            return "Authentication failed. Invalid username or password.";
        }
    }

    // 구매 제안
    @PostMapping("/{itemId}/purchaseOffer")
    public String purchaseOfferItem(
            @PathVariable("itemId") Long id,
            @RequestBody OfferDto offerDto
    ) {
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // 이제 userDetails를 사용하여 사용자 정보를 가져올 수 있습니다.
            String username = userDetails.getUsername();

            return itemService.purchaseOffer(id, offerDto,username);
        } else {
            // username과 password가 일치하지 않을 경우 처리
            return "Authentication failed. Invalid username or password.";
        }
    }

    // 구매 제안 조회 - 물품 등록자와, 제안 등록자만
    @GetMapping("/{itemId}/offer/read")
    public List<OfferDto> readOffer(
            @PathVariable("itemId") Long id
    ) throws ItemNotFoundException {
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // 이제 userDetails를 사용하여 사용자 정보를 가져올 수 있습니다.
            String username = userDetails.getUsername();

            return itemService.readOffer(id, username);
        } else {
            // username과 password가 일치하지 않을 경우 처리
            throw new AuthenticationFailedException("Authentication failed. Invalid username or password.");
        }
    }

    // 물품 등록한 사용자 - 제안 수락, 거절
    @PostMapping("/{itemId}/offer/{offerId}/accept-refuse")
    public String offerAcceptRefuse(
            @PathVariable("itemId") Long itemId,
            @PathVariable("offerId") Long offerId,
            @RequestParam("acceptRefuse") String acceptRefuse
    ) {
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // 이제 userDetails를 사용하여 사용자 정보를 가져올 수 있습니다.
            String username = userDetails.getUsername();

            return itemService.offerAcceptRefuse(itemId, offerId, username, acceptRefuse);
        } else {
            throw new AuthenticationFailedException("Authentication failed. Invalid username or password.");
        }
    }

    // 구매 확정 - 대상 물품의 상태는 판매 완료, 다른 구매 제안의 상태는 거절
    @PostMapping("/{itemId}/offer/{offerId}/confirm")
    public String offerConfirm(
            @PathVariable("itemId") Long itemId,
            @PathVariable("offerId") Long offerId
    ) {
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // 이제 userDetails를 사용하여 사용자 정보를 가져올 수 있습니다.
            String username = userDetails.getUsername();

            return itemService.offerConfirm(itemId, offerId, username);
        } else {
            throw new AuthenticationFailedException("Authentication failed. Invalid username or password.");
        }
    }

}

