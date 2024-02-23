package com.example.Mission_shop.service;

import com.example.Mission_shop.entity.CustomUserDetails;
import com.example.Mission_shop.entity.UserEntity;
import com.example.Mission_shop.repo.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@Service
public class JpaUserDetailsManager implements UserDetailsManager {
    private final UserRepository userRepository;

    public JpaUserDetailsManager (
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;

/*         //(토큰 발급) 테스트 목적의 사용자 추가
        createUser(CustomUserDetails.builder()
                .username("user")
                .password(passwordEncoder.encode("password"))
                .email("user1@gamil.com")
                .phone("01012345678")
                .authorities("ROLE_USER,READ_AUTHORITY")
                .build());*/
    }

    @Override
    // Spring Security에서 인증을 처리할 때 사용하는 메서드
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        Optional<UserEntity> optionalUser
                = userRepository.findByUsername(username);
        if (optionalUser.isEmpty())
            throw new UsernameNotFoundException(username);

        UserEntity userEntity = optionalUser.get();
        return CustomUserDetails.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .email(userEntity.getEmail())
                .phone(userEntity.getPhone())
                .authorities(userEntity.getAuthorities())
                .build();
    }

    @Override
    // 편의를 위해 같은 규약으로 회원가입을 하는 메서드
    public void createUser(UserDetails user) {
        if (userExists(user.getUsername())) // 이미 같은 이름의 사용자가 있다는 뜻 -> 오류
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        try{
            CustomUserDetails userDetails =
                    (CustomUserDetails) user;
            UserEntity newUser = UserEntity.builder()
                    .username(userDetails.getUsername())
                    .password(userDetails.getPassword())
                    .email(userDetails.getEmail())
                    .phone(userDetails.getPhone())
                    .authorities("ROLE_INACTIVE") // 회원가입만 할 경우 비활성 사용자
                    .build();
            userRepository.save(newUser);
        }catch (ClassCastException e){
            log.error("Failed Cast to: {}", CustomUserDetails.class);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void updateUser(UserDetails user) {
        // 수정하려는 사용자 확인
        if (!userExists(user.getUsername())) {
            throw new UsernameNotFoundException(user.getUsername());
        }

        // 사용자 정보 업데이트
        try {
            CustomUserDetails userDetails = (CustomUserDetails) user;
            UserEntity userEntity = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException(user.getUsername()));

            // 업데이트할 사용자 정보 추출
            String nickname = userDetails.getNickname();
            String name = userDetails.getName();
            Integer age = userEntity.getAge();
            String email = userDetails.getEmail();
            String phone = userDetails.getPhone();

            // 엔티티에 새로운 정보 반영
            userEntity.setNickname(nickname);
            userEntity.setName(name);
            userEntity.setAge(age);
            userEntity.setEmail(email);
            userEntity.setPhone(phone);

            // 사용자의 권한을 ROLE_USER로 변경
            userEntity.setAuthorities("ROLE_USER");

            // 엔티티 저장
            userRepository.save(userEntity);
        } catch (ClassCastException e) {
            log.error("Failed Cast to: {}", CustomUserDetails.class);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void BusinessUser(UserDetails user) {
        // 수정하려는 사용자 확인
        if (!userExists(user.getUsername())) {
            throw new UsernameNotFoundException(user.getUsername());
        }

        // 사용자 정보 업데이트
        try {
            CustomUserDetails userDetails = (CustomUserDetails) user;
            UserEntity userEntity = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException(user.getUsername()));

            String businessNumber = userDetails.getBusinessNumber();
            userEntity.setBusinessNumber(businessNumber);

            userEntity.setAuthorities("ROLE_BUSINESS");
            userRepository.save(userEntity);
        }catch (ClassCastException e) {
            log.error("Failed Cast to: {}", CustomUserDetails.class);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }


    // 나중에 구현 - 요구사항에 보이지 않음
    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }
}
