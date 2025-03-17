package com.korea.shopItem.Controller;

import com.korea.shopItem.dto.ItemDTO;
import com.korea.shopItem.service.ItemService;
import com.korea.shopItem.util.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/items")
public class ItemController {

    private final CustomFileUtil fileUtil;
    private final ItemService itemService;

    // 페이징 목록 조회
    @GetMapping("/list")
    public ResponseEntity<Page<ItemDTO>> getAllItems(){
        return null;
    }

    // 단일 데이터 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> getItem(@PathVariable Long id) {
        try {
            return null;
        } catch (IllegalArgumentException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }


    // 특정 아이템의 이미지 리스트 조회 API (별도 서비스 분리 X)
    @GetMapping("/view/{fileName}")
    public ResponseEntity<?> getItemImages(@PathVariable String fileName) {

        try {
            return null;

        }catch (Exception e) {
            return null;
        }

    }


    // 아이템 등록
    @PostMapping("/add")
    public ResponseEntity<ItemDTO> register(
    ) {

        try {

            return null;


        } catch (Exception e) {
            return null;

        }
    }

    // ✅ 아이템 수정
    @PutMapping("/modify/{id}")
    public ResponseEntity<ItemDTO> updateItem() {
        return null;
    }

    // ✅ 아이템 삭제 (논리 삭제)
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteItem() {
        try {
            return null;
        } catch (Exception e) {
            return null;
        }
    }


}
