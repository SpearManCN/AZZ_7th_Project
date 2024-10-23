package com.azz.azz.CONTROLLER;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RequestMapping("/api")
@RestController
public class ApiController {
    private static final String UPLOAD_DIR = "C:/uploads/";
    @PostMapping("/files/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file){
        try{
            String contentType = file.getContentType();
            if (contentType == null || !isImageFile(contentType)) {
                return ResponseEntity.badRequest().body("이미지 파일만 업로드할 수 있습니다.");
            }
            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            file.transferTo(new File(dir, file.getOriginalFilename()));
            return ResponseEntity.ok("파일이 성공적으로 업로드되었습니다: " + file.getOriginalFilename());
        }catch (IOException e){
            return ResponseEntity.status(500).body("파일 업로드 중 오류 발생: " + e.getMessage());
        }
    }
    private boolean isImageFile(String contentType) {
        return contentType.equals("image/jpeg") ||
                contentType.equals("image/png") ||
                contentType.equals("image/gif");
    }
}
