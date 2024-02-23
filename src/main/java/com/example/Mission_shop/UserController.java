package com.example.Mission_shop;

import com.example.Mission_shop.entity.CustomUserDetails;
import com.example.Mission_shop.service.JpaUserDetailsManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsManager manager;
    private final JpaUserDetailsManager userDetailsManager; // JpaUserDetailsManager로 수정

    // 회원가입
    @PostMapping("/register")
    public String signUpRequest (
            @RequestParam("username")
            String username,
            @RequestParam("password")
            String password,
            @RequestParam("password-check")
            String passwordCheck
    ) {
        if (password.equals(passwordCheck)){
            manager.createUser(CustomUserDetails.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .build()
            );
        }
        return "register done";
    }

    // 필수 정보 업데이트 : 비활성 사용자 -> 일반 사용자
    // username과 password가 일치하면 필수정보 update
    @PostMapping("/update")
    public String updateRequest(
            @RequestParam("username")
            String username,
            @RequestParam("password")
            String password,
            @RequestParam("nickname")
            String nickname,
            @RequestParam("name")
            String name,
            @RequestParam("age")
            Integer age,
            @RequestParam("email")
            String email,
            @RequestParam("phone")
            String phone
    ) {
        UserDetails userDetails = manager.loadUserByUsername(username);
        if (userDetails instanceof CustomUserDetails) {
            CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
            if (passwordEncoder.matches(password, customUserDetails.getPassword())) {
                customUserDetails.setNickname(nickname);
                customUserDetails.setName(name);
                customUserDetails.setAge(age);
                customUserDetails.setEmail(email);
                customUserDetails.setPhone(phone);
                manager.updateUser(customUserDetails);
                return "User information updated successfully";
            } else {
                return "Incorrect password";
            }
        } else {
            return "User details not found";
        }
    }

    // 일반 사용자 -> 사업자 사용자로 변환
    @PostMapping("/business")
    public String businessRegister(
            @RequestParam("username")
            String username,
            @RequestParam("password")
            String password,
            @RequestParam("businessNumber")
                String businessNumber
    ) {
        UserDetails userDetails = userDetailsManager.loadUserByUsername(username);
        if (userDetails instanceof CustomUserDetails) {
            CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
            if (passwordEncoder.matches(password, customUserDetails.getPassword())) {
                customUserDetails.setBusinessNumber(businessNumber);
                userDetailsManager.BusinessUser(customUserDetails); // updateUser 메서드로 변경
                return "User Business Number updated successfully";
            } else {
                return "Incorrect password";
            }
        } else {
            return "User details not found";
        }
    }
}
