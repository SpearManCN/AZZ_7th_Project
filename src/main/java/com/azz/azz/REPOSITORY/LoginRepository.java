package com.azz.azz.REPOSITORY;

import com.azz.azz.DOMAIN.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepository extends JpaRepository<Member, Integer> {
    public Member save(Member member);
    public Member findByEmailLeftAndEmailRightAndSocialAndPassword(String emailLeft, String emailRight, String social, String password);
    public Optional<Member> findByEmailLeftAndEmailRightAndSocial(String emailLeft, String emailRight, String social);
    public Optional<Member> findByEmailLeftAndEmailRight(String emailLeft, String emailRight);
    public Member findByPhone(String phone);
}


