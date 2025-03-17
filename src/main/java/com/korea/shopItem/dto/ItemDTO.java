package com.korea.shopItem.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.korea.shopItem.domain.Item;
import com.korea.shopItem.domain.ItemImage;
import com.korea.shopItem.domain.ItemInfo;
import com.korea.shopItem.domain.ItemOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDTO {

    private Long id;
    private String name;
    private int price;
    private int stockQty;
    private boolean delFlag;

    @Builder.Default
    private Map<String, List<String>> options = new HashMap<>();

    @Builder.Default
    private Map<String, String> info = new HashMap<>();

    @JsonIgnore
    @Builder.Default
    private List<MultipartFile> files = new ArrayList<>();

    @Builder.Default
    private List<String> uploadFileNames = new ArrayList<>();

    // ✅ 대표 이미지 1개만 가져오는 생성자
    public ItemDTO(Item item, List<ItemImage> images) {
        this.id = item.getId();
        this.name = item.getName();
        this.price = item.getPrice();
        this.stockQty = item.getStockQty();
        this.delFlag = item.isDelFlag();

        // 대표 이미지 가져오기
        this.uploadFileNames = (images != null && !images.isEmpty())
                ? List.of(images.get(0).getFileName())
                : List.of("default.png");
    }

    // ✅ 전체 정보 다 가져오는 생성자 (info 필드 수정)
    public ItemDTO(Item item, List<ItemImage> images, List<ItemOption> options, List<ItemInfo> infoList) {
        this.id = item.getId();
        this.name= item.getName();
        this.price = item.getPrice();
        this.stockQty = item.getStockQty();
        this.delFlag = item.isDelFlag();

        // 옵션 변환
        this.options = (options != null && !options.isEmpty())
                ? options.stream()
                .collect(Collectors.groupingBy(
                        ItemOption::getOptionName,
                        Collectors.mapping(ItemOption::getOptionValue, Collectors.toList())
                ))
                : Map.of();

        // ✅ `infoList`가 null이 아닐 때만 변환하여 `Map<String, String>` 형태로 저장
        this.info = (infoList != null && !infoList.isEmpty())
                ? infoList.stream()
                .collect(Collectors.toMap(
                        ItemInfo::getInfoKey,
                        ItemInfo::getInfoValue
                ))
                : Map.of();

        // 파일 이름 리스트 변환
        this.uploadFileNames = (images != null && !images.isEmpty())
                ? images.stream().map(ItemImage::getFileName).toList()
                : List.of("default.png");
    }
}
