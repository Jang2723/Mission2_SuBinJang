package com.example.Mission_shop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class OrderShopItem {
    // shop item 거래 테이블
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "shop_item_id")
    private ShopItem shopItem;

    @Setter
    @Column(nullable = false)
    private Integer amount;

    @Setter
    @Column(nullable = false)
    private Integer totalPrice;

    @Setter
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;


    @Setter
    @Column(nullable = false)
    private LocalDateTime dateTime;
}
