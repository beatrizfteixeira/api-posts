package bia.apiassyncactivemq.service;

import bia.apiassyncactivemq.entity.Comment;
import bia.apiassyncactivemq.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeignService {
    @Autowired
    private FeignInterface feign;

    public Post fetchPost(Integer id){
        return feign.getPost(id);
    }
    public List<Comment> fetchComments(Integer id){
        return feign.getComments(id);
    }
}
