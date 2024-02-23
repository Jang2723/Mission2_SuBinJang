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
    private Integer age;
    @Setter
    private String email;
    @Setter
    private String phone;

    // 권한
    // test를 위해서 문자열 하나에 ','로 구분해 권한을 묘사
    // ROLE_USER, ROLE_ADMIN, READ_AUTHORITY, WRITE_AUTHORITY -> 나중에 다시 정의
    private String authorities;
}
