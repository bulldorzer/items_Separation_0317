package com.korea.shopItem.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder // 생성자 호출하지 않고 메서드 체인 이용해서 객체 생성,초기화 함
@ToString(exclude = {"info", "images"}) // 순환참조 방지, 연결관계 삭제
public class Item {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQty;
    private boolean delFlag;

    // 아이템 인포
    @ElementCollection
    @CollectionTable(name = "item_info", joinColumns = @JoinColumn(name = "item_id")) // 테이블 이름 정해주고 조인 관계
    @Builder.Default
    @Embedded
    private List<ItemInfo> info = new ArrayList<>();

    // 아이템 옵션
    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "item_id") // FK 설정하는 것 중복관리일시에 insert,update 잠금
    private List<ItemOption> options = new ArrayList<>();
    
    
    // 아이템 이미지
    // cascade(흐르다) 영속성 컨텍스트와 관계를 가짐
    // 저장, 병합, 삭제, 연결끊기등등
    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "item_id")
    private List<ItemImage> images= new ArrayList<>();


    // 재고 증가
    public void addStock(int qty){
        this.stockQty += qty;
    }

    // 재고 삭제
    public void removeStock(int qty) throws Exception {
        int remainingStock = this.stockQty - qty;
        if(remainingStock < 0){
            throw new Exception("need more stock");
        }
        this.stockQty = remainingStock;
    }

    // 이미지 추가
    public void addImage(ItemImage image) {
        image.setOrd(this.images.size());
        this.images.add(image);
    }


    // 인포 추가
    public  void addInfo(ItemInfo info){
        this.info.add(info);
    }

    // 인포 추가
    public  void addOption(ItemOption option){
        this.options.add(option);
    }

    // 이미지 파일명 추가
    public void addImageString(String fileName){
        ItemImage itemImage = ItemImage.builder()
                .fileName(fileName)
                .build();
        addImage(itemImage);
    }

    // 이미지 삭제
    public void clearList() {
        this.images.clear();
    }


    // 변경 메서드
    public void changeName(String name) {
        this.name = name;
    }
    public void changePrice(int price) {
        this.price = price;
    }
    public void changeStockQty(int stockQty) {
        this.stockQty = stockQty;
    }
    public void changeDelFlag(boolean delFlag) {
        this.delFlag = delFlag;
    }

}
