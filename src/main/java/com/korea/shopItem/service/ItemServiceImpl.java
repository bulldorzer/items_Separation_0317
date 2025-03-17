package com.korea.shopItem.service;

import com.korea.shopItem.domain.Item;
import com.korea.shopItem.domain.ItemImage;
import com.korea.shopItem.domain.ItemInfo;
import com.korea.shopItem.domain.ItemOption;
import com.korea.shopItem.dto.ItemDTO;
import com.korea.shopItem.repository.ItemImageRepository;
import com.korea.shopItem.repository.ItemOptionRepository;
import com.korea.shopItem.repository.ItemRepository;
import com.korea.shopItem.util.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class ItemServiceImpl implements ItemService {





    // ✅ 아이템 등록
    @Override
    public ItemDTO createItem(ItemDTO itemDTO, List<MultipartFile> files) {

        return null;
    }


    // ✅ 페이징 조회 - 목록+이미지
    @Override
    public Page<ItemDTO> getAllItems(Pageable pageable) {
        return null;
    }

    // ✅ 1개 데이터 조회 - 아이템+이미지+인포+옵션
    @Override
    public ItemDTO getOne(Long id) {
        return null;
    }

    // ✅ 이미지만 조회
    @Override
    public ResponseEntity<Resource> getImageUrlByFileName(String fileName) {
        return null;
    }

    // ✅ 아이템 정보 수정
    @Override
    public ItemDTO updateItem(Long id, ItemDTO itemDTO) {
        return null;
    }

    // ✅ 논리적 삭제
    @Override
    public void deleteItem(Long id) {
        return null;
    }
}
