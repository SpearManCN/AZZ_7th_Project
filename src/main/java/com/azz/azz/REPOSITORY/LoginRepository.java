package com.azz.azz.REPOSITORY;

import com.azz.azz.DOMAIN.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepository extends JpaRepository<Member, Integer> {
    public Member save(Member member);
    public Member findByEmailLeftAndEmailRightAndSocialAndPassword(String emailLeft, String emailRight, String social, String password);
    public Optional<Member> findByEmailLeftAndEmailRightAndSocial(String emailLeft, String emailRight, String social);
    public Optional<Member> findByEmailLeftAndEmailRight(String emailLeft, String emailRight);
    public Member findByPhone(String phone);
    public Member findByEmailLeftAndEmailRightAndPhoneAndNameAndBirth(String emailLeft, String emailRight, String phone,String name, String birth);
    @Modifying
    @Query("Update Member m set m.password = :password where m.no = :no")
    public int updatePassword(@Param("password") String password, @Param("no") int no);
}


