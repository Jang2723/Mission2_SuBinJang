package com.example.Mission_shop.dto;

import com.example.Mission_shop.entity.Shop;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShopDto {
    private Long id;

    // 쇼핑몰 이름
    @Setter
    @Column(nullable = false)
    @NotBlank
    private String name;

    // 쇼핑몰 소개
    @Setter
    @Column(nullable = false)
    @NotBlank
    private String introduction;

    // 소핑몰 분류 (최소 5개 : 의류, 뷰티, 스포츠 용품, 식품, 도서 )
    @Setter
    @Column(nullable = false)
    @NotBlank
    private String category;

    // 쇼핑몰 상태
    @Setter
    private String status; // 준비중, 오픈

    // 폐쇄 요청 상태: 신청, 수락, 거절
    @Setter
    private String closureStatus;

    // 폐쇄 요청 사유
    @Setter
    private String closureReason;

    // 관리가자가 입력해줌, 쇼핑몰 주인이 볼 수 있도록
    @Setter
    private String refuseReason;

    public static ShopDto fromEntity (Shop entity) {
        return ShopDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .introduction(entity.getIntroduction())
                .category(entity.getCategory())
                .status(entity.getStatus())
                .closureStatus(entity.getClosureStatus())
                .closureReason(entity.getClosureReason())
                .refuseReason(entity.getRefuseReason())
                .build();
    }
}