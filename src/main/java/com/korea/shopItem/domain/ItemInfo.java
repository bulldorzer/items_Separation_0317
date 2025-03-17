package com.korea.shopItem.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


// 값타입 - 다른클래스의 필드로 들어간다
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class ItemInfo {

    // pk 필요하지 않음
    // 원산지(infoKey) : 말레이시아(infoValue)
    private String infoKey;
    private String infoValue;


}
