package com.example.Mission_shop.dto;

import com.example.Mission_shop.entity.Item;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private Long id;

    @Setter
    private String title; // 제목
    @Setter
    private String description; // 설명
    @Setter
    private Integer minimumPrice; // 최소 가격

    @Setter
    private String status; // 최초 등록시 상태는 판매중

    public static ItemDto fromEntity (Item entity){
        return ItemDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .minimumPrice(entity.getMinimumPrice())
                .build();
    }
}
