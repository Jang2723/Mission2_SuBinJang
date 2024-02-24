package com.example.Mission_shop.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String title; // 제목
    @Setter
    private String description; // 설명
    @Setter
    private Integer minimumPrice; // 최소 가격

    @Setter
    private String status; // 최초 등록시 상태는 판매중

    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id") // 작성자의 username을 저장하는 컬럼
    private UserEntity user;

    // 반드시 등록 필요는 없음 (지금 구현 x)
    // private String image; // 대표 이미지
}
