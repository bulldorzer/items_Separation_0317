package com.korea.shopItem.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder // 생성자 호출하지 않고 메서드 체인 이용해서 객체 생성,초기화 함
public class ItemOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="item_option_id")
    private Long id;

    @Column(name = "option_name")
    private String optionName;

    @Column(name = "option_value")
    private String optionValue;

    @Column(name = "item_id") // FK 라는 지정
    private Long itemId;

    public ItemOption(String optionName, String optionValue) {
        this.optionName = optionName;
        this.optionValue = optionValue;
    }
}
