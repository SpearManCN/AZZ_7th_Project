package com.azz.azz.DOMAIN;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Member{
    @Id
    @GeneratedValue
    private int no;
    private int social; // 0 이면 일반, 1은 카카오 ...
    private String name;
    private String birth;
    private String password;
    private String emailBefore;
    private String emailAfter;
    private String phone;
    private String addressMain;
    private String addressSub;

}
