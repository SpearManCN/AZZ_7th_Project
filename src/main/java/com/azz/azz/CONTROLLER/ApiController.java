package com.azz.azz.CONTROLLER;

import com.azz.azz.DOMAIN.Member;
import com.azz.azz.SERVICE.CustomUserDetailsService;
import com.azz.azz.SERVICE.LoginService;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
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
    public void kakaoAuthCallback(String code, HttpServletResponse resp) throws IOException{
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
        System.out.println("email = "+email + " and name = "+name);
        int location = email.indexOf("@");
        String emailLeft = email.substring(0,location);
        String emailRight = email.substring(location+1);
        Member member = new Member();
        member.setSocial(1);
        member.setName(name);
        member.setEmailLeft(emailLeft);
        member.setEmailRight(emailRight);
        member.setPassword("1");

        if(loginService.confirmEmail(member) == 1){ // 가입 가능
            loginService.signUp(member);
            System.out.println("sign up success");
        }else{
            System.out.println("sign up failed");
        }
        System.out.println("sign up failed232323");

        HttpHeaders headers2 = new HttpHeaders();
        headers2.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> formParams = new LinkedMultiValueMap<>();
        formParams.add("username", email+"@kakao@");
        formParams.add("password", "1");
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formParams, headers2);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            System.out.println("로그인 성공: " + authentication.getName());
        } else {
            System.out.println("로그인 실패");
        }
        ResponseEntity<String> response2 = restTemplate.exchange(
                "http://localhost:8080/signIn",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        Authentication authentication2 = SecurityContextHolder.getContext().getAuthentication();
        if (authentication2 != null && authentication2.isAuthenticated()) {
            System.out.println("로그인 성공: " + authentication2.getName());
        } else {
            System.out.println("로그인 실패");
        }

        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println("성공");
            // 성공 시 세션 쿠키 추출
            String setCookie = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
            if (setCookie != null) {
                headers.add(HttpHeaders.COOKIE, setCookie);

                // 리다이렉트 URL로 이동
                URI redirectUri2 = UriComponentsBuilder.fromUriString("http://localhost:8080/home").build().toUri();
                HttpEntity<Void> redirectEntity = new HttpEntity<>(headers);
                restTemplate.exchange(redirectUri2, HttpMethod.GET, redirectEntity, String.class);

                System.out.println("Redirected to: " + "localhost:8080/home");
            }
            System.out.println("성공2");
        } else {
            System.out.println("Login failed with status: " + response2.getStatusCode());
        }

//        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email+"@kakao@");
//        System.out.println("errror?1?");
//        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
//                userDetails,
//                userDetails.getPassword(),
//                userDetails.getAuthorities()
//        );
//        System.out.println("errror?2?");
//        SecurityContextHolder.getContext().setAuthentication(auth);
//        System.out.println("errror??");
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.isAuthenticated()) {
//            System.out.println("로그인 성공: " + authentication.getName());
//        } else {
//            System.out.println("로그인 실패");
//        }
//
//        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
//            // 사용자가 로그인한 상태
//            System.out.println("로그인한 상태");
//        }else{
//            System.out.println("로그인 안한 상태");
//        }
//
//        resp.sendRedirect("/");
    }

    public String getKakaoUserInfo(String accessToken) {
        String url = "https://kapi.kakao.com/v2/user/me";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
        System.out.println(response.getBody());
        return response.getBody(); // 사용자 정보 JSON
    }
}
