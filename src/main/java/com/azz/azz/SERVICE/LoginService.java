package com.azz.azz.SERVICE;

import com.azz.azz.DOMAIN.Member;
import com.azz.azz.REPOSITORY.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class LoginService {
    @Autowired
    private LoginRepository loginRepository;

    public int signUp(Member member){
        if(loginRepository.findByEmailLeftAndEmailRight(member.getEmailLeft(),member.getEmailRight())!=null){
            return 1; // 메일 중복시 1 반환
        }
        if(loginRepository.save(member)==null){
            return 2; // 저장이 실패시 2 반환
        }
        return 3; // 저장 성공시 3 반환
    }

    public int confirmPhone(Member member){
        if(loginRepository.findByPhone(member.getPhone())==null){
            return 1; // 해당 번호가 없음
        }else{
            return 0; // 중복된 번호가 이미 존재.
        }
    }
    public int confirmEmail(Member member){
        if(loginRepository.findByEmailLeftAndEmailRight(member.getEmailLeft(), member.getEmailRight())==null){
            return 1; // 해당 메일이 없음
        }else{
            return 0; // 중복된 메일이 이미 존재.
        }
    }

}
