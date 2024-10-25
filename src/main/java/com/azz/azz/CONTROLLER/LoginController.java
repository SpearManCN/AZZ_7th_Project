package com.azz.azz.CONTROLLER;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/login")
@Controller
public class LoginController {
    @ResponseBody
    @PostMapping("/signUp")
    public int signUp(){
        return 0;
    }
}
