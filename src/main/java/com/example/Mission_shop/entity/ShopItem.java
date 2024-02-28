package com.example.Mission_shop.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ShopItem {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    // 필수 정보
    // 상품 이름
    @Setter
    @Column(nullable = false)
    @NotBlank
    private String name;

    // 상품 설명
    @Setter
    @Column(nullable = false)
    @NotBlank
    private String description;

    // 상품 가격
    @Setter
    @Column(nullable = false)
    @Min(0)
    private Integer price;

    // ex) shop category : 의류
    // 상품 분류    : 남자, 여자, 아이
    @Setter
    @Column(nullable = false)
    @NotBlank
    private String mainCategory;

    // 상품 소분류   : 셔츠, 바지, 치마, 신발, 악세사리
    @Setter
    @Column(nullable = false)
    @NotBlank
    private String subCategory;

    // 상품 재고
    @Setter
    @Column(nullable = false)
    @Min(0)
    private Integer stock;


    /*// 상품 이미지 - 나중에 처리
    @Setter
    private String image;*/


    @Setter
    @ManyToOne
    @JoinColumn(name = "shop_id") // shop id
    private Shop shop;
}
