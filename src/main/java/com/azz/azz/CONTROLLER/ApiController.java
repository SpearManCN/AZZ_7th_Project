package com.azz.azz.CONTROLLER;

import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RequestMapping
@RestController
public class ApiController {
    private static final String UPLOAD_DIR = "C:/uploads/";
    @PostMapping("/api/files/upload")
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
    @GetMapping("/kakaoLogin")
    public void kakaoAuth(HttpServletResponse response) throws IOException{
        String kakaoLoginUrl = getKakaoLoginUrl();
        System.out.println(kakaoLoginUrl);
        response.sendRedirect(kakaoLoginUrl);

    }

    public String getKakaoLoginUrl() {
        String clientId = "089f7b2b7117e7dfafb58a1638cc179e"; // 카카오 개발자 사이트에서 발급받은 REST API 키
        String redirectUri = "http://localhost:8080/api/kakaoAuth"; // 인가 코드를 받을 URI
//        String state = "YOUR_STATE"; // CSRF 방지를 위한 임의의 값

        return "https://kauth.kakao.com/oauth/authorize?client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code&state=" ;
    }

    @GetMapping("/api/kakaoAuth")
    public ResponseEntity<String> kakaoAuthCallback(String code){
        String clientId = "089f7b2b7117e7dfafb58a1638cc179e"; // 카카오 개발자 사이트에서 발급받은 REST API 키
        String redirectUri = "http://localhost:8080/api/kakaoAuth"; // 인가 코드를 받을 URI
        String url = "https://kauth.kakao.com/oauth/token";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        String accessToken = new JSONObject(response.getBody()).getString("access_token");

        String userInfoJson = getKakaoUserInfo(accessToken);



        return ResponseEntity.ok("Access Token: " + accessToken);
    }

    public String getKakaoUserInfo(String accessToken) {
        String url = "https://kapi.kakao.com/v2/user/me";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        return response.getBody(); // 사용자 정보 JSON
    }
}
