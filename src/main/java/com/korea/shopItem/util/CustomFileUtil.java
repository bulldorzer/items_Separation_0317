package com.korea.shopItem.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Log4j2
@RequiredArgsConstructor
public class CustomFileUtil {

    @Value("${com.korea.upload.path}")
    private String uploadPath;
    private static final String DEFAULT_IMAGE = "default.png";

    @PostConstruct
    public void init(){
        File tempFolder = new File(uploadPath);

        if(!tempFolder.exists()){
            boolean wasSuccessful = tempFolder.mkdirs();  // 상위 폴더까지 자동 생성
            if (!wasSuccessful) {
                log.error("파일 업로드 폴더 생성 실패: " + uploadPath);
                throw new RuntimeException("업로드 폴더를 생성하지 못했습니다.");
            }
        }

        uploadPath = tempFolder.getAbsolutePath();

        log.info("------------------------------");
        log.info(uploadPath);
    }

    // 파일 저장해주고 이름값을 추출하여 반환함
    public List<String> saveFiles(List<MultipartFile> files) throws RuntimeException{

        List<String> uploadNames = new ArrayList<>();

        if(files == null || files.isEmpty()){
            Path defaultImagePath = Paths.get(uploadPath, DEFAULT_IMAGE);
            if(Files.exists(defaultImagePath)){
                uploadNames.add(DEFAULT_IMAGE);
            } else {
                log.warn("기본 이미지 파일이 존재하지 않습니다." + DEFAULT_IMAGE);
                throw new RuntimeException("기본 이미지가 존재하지 않습니다.");
            }
            return uploadNames;
        }

        for(MultipartFile multipartFile : files){
            String savedName = UUID.randomUUID().toString() + "_" + multipartFile.getOriginalFilename();
            Path savePath = Paths.get(uploadPath, savedName);

            try {
                Files.copy( multipartFile.getInputStream(), savePath);

                // 썸네일 생성
                String contentType = multipartFile.getContentType();
                if( contentType != null && contentType.startsWith("image")){
                    Path thumbnailPath = Paths.get(uploadPath, "s_" + savedName);
                    Thumbnails.of(savePath.toFile())
                            .size(200, 200)
                            .toFile(thumbnailPath.toFile());
                }
                uploadNames.add(savedName);

                // 파일이 정상적으로 저장되었는지 확인
                if (!Files.exists(savePath)) {
                    throw new RuntimeException("파일이 정상적으로 저장되지 않았습니다: " + savePath.toAbsolutePath());
                }

            } catch (IOException e){
                log.error("파일 저장 실패: " + e.getMessage());
                throw  new RuntimeException(e.getMessage());
            }
        }
        return uploadNames;
    }

    public ResponseEntity<Resource> getFile(String fileName) {

        if(uploadPath == null || uploadPath.isBlank()){
            log.error("uploadPath가 설정되지 않았습니다.");
            return ResponseEntity.internalServerError().build();
        }

        Path filePath = Paths.get(uploadPath, fileName);
        log.info("요청된 파일 경로: " + filePath.toAbsolutePath());
        Resource resource = new FileSystemResource(filePath);

        if (!resource.exists() || !resource.isReadable()) {
            log.warn("파일을 찾을 수 없음 또는 읽을 수 없음: " + filePath.toAbsolutePath());
            filePath = Paths.get(uploadPath, "default.png");

            resource = new FileSystemResource(filePath);

            if (!resource.exists() || !resource.isReadable()) {
                log.error("기본 이미지 파일도 존재하지 않음: " + filePath.toAbsolutePath());
                return ResponseEntity.notFound().build();
            }
        }

        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", Files.probeContentType(filePath));
        } catch (IOException e) {
            log.error("파일 타입 조회 실패: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok().headers(headers).body(resource);
    }

    public void deleteFiles(List<String> fileNames){
        if(fileNames == null || fileNames.isEmpty()){
            return;
        }
        fileNames.forEach(fileName ->{
            String thumbnailFileName = "s_" + fileNames;
            Path thumbnailPath = Paths.get(uploadPath, thumbnailFileName);
            Path filePath = Paths.get(uploadPath, fileName);

            try{
                Files.deleteIfExists(filePath);
                Files.deleteIfExists(thumbnailPath);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        });
    }
}
