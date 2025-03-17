package com.korea.shopItem.service;


import com.korea.shopItem.dto.ItemDTO;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ItemService {

    public ItemDTO createItem(ItemDTO itemDTO, List<MultipartFile> files);
    public Page<ItemDTO> getAllItems(Pageable pageable);
    public ResponseEntity<Resource> getImageUrlByFileName(String fileName);
    public ItemDTO getOne(Long id);
    public ItemDTO updateItem(Long id, ItemDTO itemDTO);
    public void deleteItem(Long id);

}
