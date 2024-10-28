package com.azz.azz.DOMAIN;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Member{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;
    private Integer social; // 0 이면 일반, 1은 카카오 ...
    private String name;
    private String birth;
    private String password;
    private String emailLeft;
    private String emailRight;
    private String phone;
    private String addressMain;
    private String addressSub;

}
