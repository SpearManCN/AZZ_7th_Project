package com.azz.azz.CONTROLLER;

import com.azz.azz.DOMAIN.Member;
import com.azz.azz.SERVICE.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/login")
@Controller
public class LoginController {
    @Autowired
    private LoginService loginService;

    @ResponseBody
    @PostMapping("/signUp")
    public String signUp(Member member){
        if(member.getSocial()==null)member.setSocial(0);
        int result = loginService.signUp(member);
        System.out.println(member.toString());
        if(result == 1){
            return "이미 가입된 메일입니다.";
        }else if(result == 2){
            return "가입이 실패하였습니다.";
        }else{
            return "가입에 성공하였습니다.";
        }

    }
    @ResponseBody
    @PostMapping("/confirmPhone")
    public int confirmPhone(Member member){
        return loginService.confirmPhone(member);
    }

    @ResponseBody
    @PostMapping("/confirmEmail")
    public int confirmEmail(Member member){
        return loginService.confirmEmail(member);
    }
}
