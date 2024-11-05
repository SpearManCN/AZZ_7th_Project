package com.azz.azz.SERVICE;

import com.azz.azz.DOMAIN.Member;
import com.azz.azz.REPOSITORY.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private LoginRepository loginRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("CustomUserDetailsService.loadUserByUsername");
        String social="0";
        if(username.contains("@kakao@")){
            username=username.replace("@kakao@","");
            social = "1";
        }
        int atIndex = username.indexOf('@');
        String beforeAt = username.substring(0, atIndex);
        String afterAt = username.substring(atIndex + 1);
        final String finalUsername = username;
        Member mm = loginRepository.findByEmailLeftAndEmailRightAndSocial(beforeAt, afterAt, social)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + finalUsername));
        return mm;
    }

}
