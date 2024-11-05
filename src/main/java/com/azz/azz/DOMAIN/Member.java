package com.azz.azz.DOMAIN;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

@Data
@Entity
public class Member implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;
    private String social; // 0 이면 일반, 1은 카카오 ...
    private String name;
    private String birth;
    private String password;
    private String emailLeft;
    private String emailRight;
    private String phone;
    private String addressMain;
    private String addressSub;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null; // 역할이 필요하면 권한 리스트 반환
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return emailLeft+"@"+emailRight;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
