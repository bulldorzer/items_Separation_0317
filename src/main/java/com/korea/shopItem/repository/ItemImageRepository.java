package com.korea.shopItem.repository;

import com.korea.shopItem.domain.ItemImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemImageRepository extends JpaRepository<ItemImage, Long> {

    List<ItemImage> findByItemId(Long itemId);
    
    // 이미지 파일명으로 이미지 가져오기
    Optional<ItemImage> findByFileName(String fileName);
}
