package com.korea.shopItem.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder // 생성자 호출하지 않고 메서드 체인 이용해서 객체 생성,초기화 함
public class ItemImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="item_image_id")
    private Long id;
    private String fileName;

    @Setter
    private int ord; // 그림 순번

    @Column(name = "item_id")
    private Long itemId;

}
