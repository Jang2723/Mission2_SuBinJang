package com.example.Mission_shop;

import com.example.Mission_shop.dto.ShopDto;
import com.example.Mission_shop.exception.AuthenticationFailedException;
import com.example.Mission_shop.service.ShopService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("shops")
@RequiredArgsConstructor
public class ShopController {
    private final ShopService shopService;

    // 쇼핑몰 생성은 business 사용자가 되면 자동 생성
    // 쇼핑몰 정보 작성자 수정
    @PostMapping("/update")
    public String updateShop (
            @RequestBody ShopDto shopDto
    ) {
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // 이제 userDetails를 사용하여 사용자 정보를 가져올 수 있습니다.
            String username = userDetails.getUsername();

            return shopService.updateShop(shopDto, username);
        } else {
            // username과 password가 일치하지 않을 경우 처리
            return "Authentication failed. Invalid username or password.";
        }
    }

    // 쇼핑몰 개설 신청
    @PostMapping("/apply/open")
    public String shopApplyOpen (){
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // 이제 userDetails를 사용하여 사용자 정보를 가져올 수 있습니다.
            String username = userDetails.getUsername();

            return shopService.shopApplyOpen(username);
        } else {
            // username과 password가 일치하지 않을 경우 처리
            return "Authentication failed. Invalid username or password.";
        }
    }

    // 관리자, 개설 신청자(자기 쇼핑몰) - 개설 신청된 쇼핑몰 목록 확인
    @GetMapping("/apply/read")
    public List<ShopDto> applyRead() {
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // 사용자 ROLE 확인
            if (userDetails.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"))) {

                // ROLE이 ADMIN일 경우 바로 목록 조회
                String username = "admin";
                return shopService.applyRead(username);
            }
            else if (userDetails.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_BUSINESS"))) {

                // ROLE이 BUSINESS일 경우 자기 쇼핑몰만 조회
                String username = userDetails.getUsername();
                return shopService.applyRead(username);
            } else {
                // 다른 권한을 가진 경우 처리
                throw new AuthenticationFailedException("Unauthorized access.");
            }
        } else {
            // 인증되지 않은 경우 에러 처리
            throw new AuthenticationFailedException("Authentication required.");
        }
    }

    // 개설신청 허가/ 불허가
    @PostMapping("/apply/acceptRefuse")
    public String acceptRefuse(
            @RequestBody ShopDto shopDto
    ) {
        return shopService.acceptRefuse(shopDto);
    }

    // 쇼핑몰 폐쇄 신청
    @PostMapping("/apply/close")
    public String shopApplyClose(
            @RequestBody ShopDto shopDto
    ) {
        // 쇼핑몰을 개설한 사용자의 정보를 가져오고 그 쇼핑몰의 정보와 사용자가 일치하면 폐쇄요청 성공
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // 이제 userDetails를 사용하여 사용자 정보를 가져올 수 있습니다.
            String username = userDetails.getUsername();

            return shopService.shopApplyClose(username, shopDto);
        } else {
            // username과 password가 일치하지 않을 경우 처리
            return "Authentication failed. Invalid username or password.";
        }

    }
}
