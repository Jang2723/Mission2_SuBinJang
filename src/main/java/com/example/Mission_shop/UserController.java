package com.example.Mission_shop;

import com.example.Mission_shop.entity.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
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
}
