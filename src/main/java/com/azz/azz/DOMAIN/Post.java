package com.azz.azz.DOMAIN;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int no;
    int member_no;
    String dates;
    String content;
    int post_type; // 전체공개 2 or 그룹에게 공개 1 or 나에게만 공개 0
    int group_no; // 그룹에게 공개일시 그룹 no
    int likes; // 좋아요 갯수.  누가 눌렀는지는 likemembers 테이블에 있음
    String tag1;
    String tag2;
    String tag3;
    String tag4;
    String tag5;
}
