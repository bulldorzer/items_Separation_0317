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


@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemOptionRepository itemOptionRepository;
    private final ItemImageRepository itemImageRepository;
    private final CustomFileUtil fileUtil;


    // ✅ 아이템 등록
    @Override
    public ItemDTO createItem(ItemDTO itemDTO, List<MultipartFile> files) {

        // DTO -> 엔티티 -> SAVE
        // 기본정보 + 옵션 Map<String, List<String> + 인포 : Map<String, String> + 이미지 : List
        // dto 데이터를 각각 뽑아서 -> 엔티티 필드에 값을 설정해야함

        // 기본정보
        Item item = Item.builder()
                .name(itemDTO.getName())
                .price(itemDTO.getPrice())
                .stockQty(itemDTO.getStockQty())
                .delFlag(itemDTO.isDelFlag()).build();
        
        // 인포, 이미지, 옵션 비어있음
        Item savedItem = itemRepository.save(item); // 저장된 엔티티 그대로 추출(itemId 생성됨)
        Long itemId = savedItem.getId();
        
        // 인포
        if (itemDTO.getInfo() != null && !itemDTO.getInfo().isEmpty()){ // 값이 있으면 처리
            
            
            itemDTO.getInfo().forEach((key,value)->{
                item.addInfo( new ItemInfo(key, value));
            });
        }
        
        // 이미지
        List<ItemImage> images = new ArrayList<>();
        if (files != null && !files.isEmpty()){ // 매개변수로 담은 이미지 첨부파일
            List<String> fileNames = fileUtil.saveFiles(files); // 파일 저장해주고 이름값을 추출하여 반환함
            images = fileNames.stream()
                    .map(fileName ->
                            ItemImage.builder()
                                    .fileName(fileName)
                                    .itemId(itemId).build()
                    ).toList();
            itemImageRepository.saveAll(images); // 인포와, 이미지이름까지 저장
        }
        
        // 옵션
        /*
             Map<String(optionName), List<String>(optionValue) >
            {
                "색상" : ["빨강","노랑","파랑"],
                "사이즈" : ["S","M","L"]
            }
        */
        List<ItemOption> options = new ArrayList<>();
        if (itemDTO.getOptions() != null && !itemDTO.getOptions().isEmpty()){
            
            // "색상" : ["빨강","노랑","파랑"]
            // entrySet의 역할 : k,v로 값을 변환
            // 하나의 stream()으로 할수 있도록 평탄화함
            // flateMap 각 List를 펼쳐서 하나의 stream으로 할수 있도록 함
            itemDTO.getOptions().entrySet().stream()
                    .flatMap( entry -> 
                            entry.getValue().stream()
                                    .map(value -> ItemOption.builder()
                                            .optionName(entry.getKey())
                                            .optionValue(value)
                                            .itemId(itemId)
                                            .build()
                                    )
                    ).toList();
            itemOptionRepository.saveAll(options);
            /*
                연산 결과
                색상 : 빨강,
                색상 : 노랑,
                색상 : 파랑,
                사이즈 : S,
                사이즈 : M,
                사이즈 : L
            */
        }
        
        // 엔티티
        
        
        return new ItemDTO(savedItem, images, options, savedItem.getInfo());
    }


    // ✅ 페이징 조회 - 목록+이미지
    @Override
    public Page<ItemDTO> getAllItems(Pageable pageable) {
        Page<Item> itemPage = itemRepository.findAllWithImages(pageable);

        return itemPage.map(item -> {
            List<ItemImage> images = item.getImages();
            ItemImage representativeImage = (images != null && !images.isEmpty())
                    ? images.get(0)
                    : ItemImage.builder().fileName("default.png").build();
            return new ItemDTO(item, List.of(representativeImage));
        });
    }

    // ✅ 1개 데이터 조회 - 아이템+이미지+인포+옵션
    @Override
    public ItemDTO getOne(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("해당 삼풍이 존재하지 않습니다, ID: "+id));

        // 개별적으로 연관 데이터를 가져옴
        List<ItemImage> images = itemImageRepository.findByItemId(id);
        List<ItemOption> options = itemOptionRepository.findByItemId(id);
        return new ItemDTO(item, images, options, item.getInfo());
    }

    // ✅ 이미지만 조회
    @Override
    public ResponseEntity<Resource> getImageUrlByFileName(String fileName) {
        Optional<ItemImage> image =itemImageRepository.findByFileName(fileName);
        if (image.isEmpty()){
            return null;
        }
        return fileUtil.getFile(fileName);
    }

    // ✅ 아이템 정보 수정
    @Override
    public ItemDTO updateItem(Long id, ItemDTO itemDTO) {
        Item item = itemRepository.findById(id).orElseThrow();
        
        if (itemDTO.getName() != null) item.changeName(item.getName());
        if (itemDTO.getPrice() >=0 ) item.changePrice(item.getPrice());
        if (itemDTO.getStockQty() >= 0) item.changeStockQty(item.getStockQty());
        
        item.changeDelFlag(itemDTO.isDelFlag());
        
        // 인포 값타임은 부분 수정이 불가함
        if (itemDTO.getInfo() != null){
            item.getInfo().clear(); //전체 삭제하고 데이터 전부 재등록
            itemDTO.getInfo().forEach((key,value) -> {
                item.addInfo(new ItemInfo(key, value));
            });
        }
        
        // 이미지
        if (itemDTO.getUploadFileNames() != null){
            item.clearList(); // 이미지를 비움
            itemDTO.getUploadFileNames().forEach( fileName -> {
                item.addImage(ItemImage.builder().fileName(fileName).build());
            });
        }
        // 옵션
        if (itemDTO.getOptions() != null){
            item.getOptions().clear(); // 기존 옵션을 비움
            itemDTO.getOptions().forEach((key,values) ->{
                values.forEach(value->{
                    item.addOption(new ItemOption(key,value));
                });
            });
        }
        itemRepository.save(item);
        
        return new ItemDTO(item, item.getImages(), item.getOptions(), item.getInfo());
    }

    // ✅ 논리적 삭제
    @Override
    public void deleteItem(Long id) {
        Item item = itemRepository.findById(id).orElseThrow();
        item.changeDelFlag(true);
        itemRepository.save(item);
        //return null;
    }
}

