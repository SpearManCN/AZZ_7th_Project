package com.azz.azz.CONTROLLER;

import com.azz.azz.DOMAIN.*;
import com.azz.azz.SERVICE.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/home")
@Controller
public class HomeController {
    @Autowired
    private PostService postService;


    @GetMapping
    public String home(Model model) {
        System.out.println("HomeController.home");
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        Member member = (Member) auth.getPrincipal();
//
//        model.addAttribute("member", member);
        List<Posts> posts = postService.getAllPosts();
        List<Pictures> pictures = new ArrayList<>();
        System.out.println("length = "+posts.size() );
        for(Posts post : posts){
            Pictures picture = new Pictures();
            picture.setPosts(post);
            List<String> newPics = new ArrayList<>();
            System.out.println("no = "+ post.getNo());
            for(String newPic :  post.getNew_picture().split(",")){
                newPics.add (newPic);
            }
            System.out.println(newPics.get(0));
            picture.setFull_name(newPics);
            pictures.add(picture);
        }
        model.addAttribute("pictures", pictures);
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
