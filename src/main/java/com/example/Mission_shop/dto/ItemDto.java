package com.example.Mission_shop.dto;

import com.example.Mission_shop.entity.Item;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private Long id;

    @Setter
    @Column(nullable = false)
    @NotBlank
    private String title; // 제목

    @Setter
    @Column(nullable = false)
    @NotBlank
    private String description; // 설명

    @Setter
    @Column(nullable = false)
    @Min(0)
    private Integer minimumPrice; // 최소 가격

    @Setter
    private String status; // 최초 등록시 상태는 판매중

    public static ItemDto fromEntity (Item entity){
        return ItemDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .minimumPrice(entity.getMinimumPrice())
                .status(entity.getStatus())
                .build();
    }
}
