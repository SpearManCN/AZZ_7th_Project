package com.azz.azz.CONTROLLER;

import com.azz.azz.DOMAIN.Posts;
import com.azz.azz.SERVICE.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Controller
public class PostController {
    @Autowired
    private PostService postService;
    private static final String UPLOAD_DIR = "C:/Users/cn105/IdeaProjects/AZZ/src/main/resources/static/assets/uploads/";
    @PostMapping(value="/post/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE} )
    public String upload(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam(value="post_type", required = false) Integer post_type,
            @RequestParam(value="member_no", required = false) Integer member_no,
            @RequestParam(value="content", required = false) String content,
            @RequestParam(value="tag1", required = false) String tag1,
            @RequestParam(value="tag2", required = false) String tag2,
            @RequestParam(value="tag3", required = false) String tag3,
            @RequestParam(value="tag4", required = false) String tag4,
            @RequestParam(value="tag5", required = false) String tag5
    ) {
        Posts post = new Posts();

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        String formattedDateTime = now.format(formatter);

        String origin_picture = "";
        String new_picture = "";
        int post_no = postService.findMaxNo();

        post.setDates(formattedDateTime);
        post.setNo(post_no+1);
        post.setPost_type(post_type);
        post.setMember_no(member_no);
        post.setTag1(tag1);
        post.setTag2(tag2);
        post.setTag3(tag3);
        post.setTag4(tag4);
        post.setTag5(tag5);
        post.setLikes(0);
        post.setLocation(UPLOAD_DIR);

        System.out.println(post.toString());

//        if(postService.uploadPost(post)==0){
//            return "home";
//        }


        File dir = new File(UPLOAD_DIR);
        boolean isFirst = true;
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try{
            for (MultipartFile file : files) {
                String newFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                if(isFirst){
                    origin_picture += file.getOriginalFilename();
                    new_picture += newFileName;
                    isFirst = false;
                }else{
                    origin_picture += "," + file.getOriginalFilename();
                    new_picture += "," + newFileName;
                }
                file.transferTo(new File(dir, newFileName));
            }
            post.setOrigin_picture(origin_picture);
            post.setNew_picture(new_picture);
        }catch (IOException e){
//            return ResponseEntity.status(500).body("파일 업로드 중 오류 발생: " + e.getMessage());
            System.out.println("catch error");
            return "homePost";
        }
//        return ResponseEntity.ok("파일이 성공적으로 업로드되었습니다: ");

        if(postService.uploadPost(post)==0){
            return "home";
        }
        System.out.println("success");
        return "home";

    }
}
