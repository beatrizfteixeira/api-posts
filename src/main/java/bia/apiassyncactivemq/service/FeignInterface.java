package bia.apiassyncactivemq.service;

import bia.apiassyncactivemq.entity.Comment;
import bia.apiassyncactivemq.entity.Post;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "json", url = "https://jsonplaceholder.typicode.com")
public interface FeignInterface {

    @GetMapping("/posts/{postId}")
    Post getPost(@PathVariable("postId") Integer postId);
    @GetMapping("/posts/{postId}/comments")
    List<Comment> getComments(@PathVariable("postId") Integer postId);
}

