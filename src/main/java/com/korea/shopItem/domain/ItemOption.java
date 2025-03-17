package com.korea.shopItem.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class ItemOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="item_option_id")
    private Long id;


    private String optionName;


    private String optionValue;


    private Long itemId;

    public ItemOption(String optionName, String optionValue) {
        this.optionName = optionName;
        this.optionValue = optionValue;
    }
}
