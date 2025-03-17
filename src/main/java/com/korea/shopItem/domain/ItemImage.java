package com.korea.shopItem.domain;

import jakarta.persistence.*;
import lombok.*;


public class ItemImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="item_image_id")
    private Long id;
    private String fileName;

    private int ord; // 그림 순번

    private Long itemId;

}
