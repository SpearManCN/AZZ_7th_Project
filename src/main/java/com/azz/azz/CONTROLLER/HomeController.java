package com.azz.azz.CONTROLLER;

import com.azz.azz.DOMAIN.Member;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/home")
@Controller
public class HomeController {
    @GetMapping
    public String home(Model model) {
        System.out.println("HomeController.home");
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        Member member = (Member) auth.getPrincipal();
//
//        model.addAttribute("member", member);
        return "home";
    }

    @GetMapping("/post")
    public String post(Model model) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        Member member = (Member) auth.getPrincipal();
//        model.addAttribute("member", member);
        return "homePost";
    }
}
