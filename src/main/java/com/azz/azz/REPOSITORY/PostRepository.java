package com.azz.azz.REPOSITORY;

import com.azz.azz.DOMAIN.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Posts, Integer> {
    public Posts save(Posts posts);
    @Query("select max(p.no) from Posts p")
    public Integer findMaxNo();
    public List<Posts> findAll();
}
