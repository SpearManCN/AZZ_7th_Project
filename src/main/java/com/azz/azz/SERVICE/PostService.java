package com.azz.azz.SERVICE;

import com.azz.azz.DOMAIN.Posts;
import com.azz.azz.REPOSITORY.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    public int uploadPost(Posts posts) {
        if(postRepository.save(posts)!=null){
            return 1;
        }else{
            return 0;
        }
    }

    public int findMaxNo(){
        if(postRepository.findMaxNo()==null){
            return 0;
        }
        return postRepository.findMaxNo();
    }

    public List<Posts> getAllPosts(){
        return postRepository.findAll();
    }
}
