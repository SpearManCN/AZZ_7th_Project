package com.azz.azz.CONTROLLER;

import com.azz.azz.DOMAIN.Member;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class IndexController {
    @GetMapping("/")
    public String index(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            System.out.println("로그인 성공: " + auth.getName());
        } else {
            System.out.println("로그인 실패");
        }


        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            // 사용자가 로그인한 상태
            return "indexUser";
        } else {
            // 사용자가 로그인하지 않은 상태
            return "index";
        }
    }
}
