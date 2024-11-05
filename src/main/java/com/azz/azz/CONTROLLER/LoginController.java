package com.azz.azz.CONTROLLER;

import com.azz.azz.DOMAIN.Member;
import com.azz.azz.SERVICE.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
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
        System.out.println("LoginController.signUp");
        if(member.getSocial()==null)member.setSocial("0");
        int result = loginService.signUp(member);
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
        System.out.println("LoginController.confirmPhone");
        return loginService.confirmPhone(member);
    }

    @ResponseBody
    @PostMapping("/confirmEmail")
    public int confirmEmail(Member member){
        System.out.println("LoginController.confirmEmail");
        return loginService.confirmEmail(member);
    }

    @ResponseBody
    @PostMapping("/findUser")
    public int findUser(Member member){
        System.out.println("LoginController.findUser");
        Member response = loginService.findUser(member);
        if(response==null){
            return -1;
        }else{
            return response.getNo();
        }
    }

    @ResponseBody
    @PostMapping("/changePw")
    public int changePw(Member member){
        System.out.println("LoginController.changePw");
        return loginService.updatePassword(member);
    }
}
