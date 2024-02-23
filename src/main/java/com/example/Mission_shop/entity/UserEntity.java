package com.example.Mission_shop.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@Table(name = "user_table")
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @Column(nullable = false, unique = true)
    private String username;
    private String password;

    // 추가하고 싶은 정보
    @Setter
    private String nickname;
    @Setter
    private String name;
    @Setter
    private Integer  age;
    @Setter
    private String email;
    @Setter
    private String phone;

    // 권한
    // test를 위해서 문자열 하나에 ','로 구분해 권한을 묘사
    // ROLE_INACTIVE (비활성 사용자 - 회원 가입만)
    // ROLE_USER (일반 사용자 - 닉네임, 이메일 등 업데이트 한 비활성 사용자)
    // ROLE_BUSINESS (사업자 사용자)
    // ROLE_ADMIN (관리자)
    @Setter
    private String authorities;

    // 사업자 등록번호
    @Setter
    private String businessNumber;
}
