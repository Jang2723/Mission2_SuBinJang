package com.example.Mission_shop;

import com.example.Mission_shop.entity.CustomUserDetails;
import com.example.Mission_shop.entity.UserEntity;
import com.example.Mission_shop.repo.UserRepository;
import com.example.Mission_shop.service.JpaUserDetailsManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsManager manager;
    private final JpaUserDetailsManager userDetailsManager; // JpaUserDetailsManager로 수정

    private final UserRepository userRepository;


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
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("nickname") String nickname,
            @RequestParam("name") String name,
            @RequestParam("age") Integer age,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone
            //@RequestParam(value = "image" , required = false) MultipartFile imageFile
    )   {
        UserDetails userDetails = manager.loadUserByUsername(username);
        if (userDetails instanceof CustomUserDetails) {
            CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
            if (passwordEncoder.matches(password, customUserDetails.getPassword())) {
                customUserDetails.setNickname(nickname);
                customUserDetails.setName(name);
                customUserDetails.setAge(age);
                customUserDetails.setEmail(email);
                customUserDetails.setPhone(phone);

                /*// 이미지 업로드 처리
                if (!imageFile.isEmpty()) {
                    saveImage(username, imageFile);
                }*/

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
            String businessNumber,
            @RequestParam("apply")
            String apply
    ) {
        UserDetails userDetails = userDetailsManager.loadUserByUsername(username);
        if (userDetails instanceof CustomUserDetails) {
            CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
            if (passwordEncoder.matches(password, customUserDetails.getPassword())) {
                customUserDetails.setBusinessNumber(businessNumber);
                customUserDetails.setApply(apply);
                userDetailsManager.BusinessUser(customUserDetails); // updateUser 메서드로 변경
                return "User Business Number updated successfully";
            } else {
                return "Incorrect password";
            }
        } else {
            return "User details not found";
        }
    }

    // 사업자 사용자 전환 신청 목록 보기
    @GetMapping("/admin/applyList")
    public List<UserEntity> applyList() {
        // UserRepository에서 apply가 "APPLY"인 사용자 목록을 가져옴
        List<UserEntity> userList = userRepository.findByApply("APPLY");

        // 사용자 목록을 반환
        return userList;
    }

    // 사업자 전환 신청 수락 - 거절
    @PostMapping("/admin/apply/accept-refuse")
    public String acceptRefuse(
            @RequestParam("businessNumber")
            String businessNumber,
            @RequestParam("acceptRefuse")
            String acceptRefuse
    ) {

        // 비즈니스 번호로 사용자 찾아내기
        Optional<UserEntity> optionalUser = userRepository.findByBusinessNumber(businessNumber);
        if (optionalUser.isEmpty()) {
            return "User not found with business number: " + businessNumber;
        }

        UserEntity userEntity = optionalUser.get();

        // apply 값 업데이트
        userEntity.setApply(acceptRefuse);

        // 권한 authorities 업데이트
        if (acceptRefuse.equals("ACCEPT")) {
            userEntity.setAuthorities("ROLE_BUSINESS");
        }

        // 엔티티 저장
        userRepository.save(userEntity);

        return "Application " + acceptRefuse + "ed successfully for user with business number: " + businessNumber;

    }
}
