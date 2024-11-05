package com.azz.azz.CONTROLLER;

import com.azz.azz.DOMAIN.Member;
import com.azz.azz.SERVICE.CustomUserDetailsService;
import com.azz.azz.SERVICE.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RequestMapping
@RestController
public class ApiController {

    private static final String UPLOAD_DIR = "C:/uploads/";
    @Autowired
    private IndexController indexController;
    @Autowired
    private LoginService loginService;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @PostMapping("/api/files/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file){
        System.out.println("ApiController.uploadFile");
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
        System.out.println("ApiController.isImageFile");
        return contentType.equals("image/jpeg") ||
                contentType.equals("image/png") ||
                contentType.equals("image/gif");
    }
    @GetMapping("/kakaoLogin")
    public void kakaoAuth(HttpServletResponse response) throws IOException{
        System.out.println("ApiController.kakaoAuth");
        String kakaoLoginUrl = getKakaoLoginUrl();
        System.out.println(kakaoLoginUrl);
        response.sendRedirect(kakaoLoginUrl);

    }

    public String getKakaoLoginUrl() {
        System.out.println("ApiController.getKakaoLoginUrl");
        String clientId = "089f7b2b7117e7dfafb58a1638cc179e"; // 카카오 개발자 사이트에서 발급받은 REST API 키
        String redirectUri = "http://localhost:8080/api/kakaoAuth"; // 인가 코드를 받을 URI
//        String state = "YOUR_STATE"; // CSRF 방지를 위한 임의의 값

        return "https://kauth.kakao.com/oauth/authorize?client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code&state=" ;
    }

    @GetMapping("/api/kakaoAuth")
    public void kakaoAuthCallback(String code, HttpServletResponse resp, HttpServletRequest req) throws IOException{
        System.out.println("ApiController.kakaoAuthCallback");
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

        JSONObject userInfo = new JSONObject(userInfoJson);
        String email = userInfo.getJSONObject("kakao_account").getString("email");
        String name = userInfo.getJSONObject("properties").getString("nickname");
        int location = email.indexOf("@");
        String emailLeft = email.substring(0,location);
        String emailRight = email.substring(location+1);
        Member member = new Member();
        member.setSocial("1");
        member.setName(name);
        member.setEmailLeft(emailLeft);
        member.setEmailRight(emailRight);
        member.setPassword("1");


        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email+"@kakao@");
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        HttpSession session = req.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);


        resp.sendRedirect("/#home");
    }

    public String getKakaoUserInfo(String accessToken) {
        System.out.println("ApiController.getKakaoUserInfo");
        String url = "https://kapi.kakao.com/v2/user/me";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
        return response.getBody(); // 사용자 정보 JSON
    }
}
