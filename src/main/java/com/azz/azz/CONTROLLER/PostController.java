package com.azz.azz.CONTROLLER;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;
import java.io.File;
import java.io.IOException;

@Controller
public class PostController {

    private static final String UPLOAD_DIR = "C:/uploads/";

    @PostMapping("/post/upload")
    public String upload(@RequestParam("files") MultipartFile[] files) {
        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try{
            for (MultipartFile file : files) {
//                String newFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                file.transferTo(new File(dir, file.getOriginalFilename()));

            }
        }catch (IOException e){
//            return ResponseEntity.status(500).body("파일 업로드 중 오류 발생: " + e.getMessage());
            return "homePost";
        }
//        return ResponseEntity.ok("파일이 성공적으로 업로드되었습니다: ");
        return "home";

    }
}
