package bia.apiassyncactivemq.controller;


import bia.apiassyncactivemq.entity.Post;
import bia.apiassyncactivemq.service.FetchPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//endpoints expostos

@RestController
public class Controller {
    @Autowired
    private FetchPostService api;
    @PostMapping("/posts/{id}")
    public ResponseEntity<String> processPost(@PathVariable (name = "id") Integer id) {
        api.getAndSavePost(id);
        return ResponseEntity.ok("Message sent to RabbitMQ...");
    }
    @GetMapping("/posts")
    @ResponseBody
    public List<Post> queryPosts() {
        List<Post> list = api.getPosts();
        return list; // Retorna a lista de posts
    }


    @PutMapping("/posts/{id}")
    public void reprocessPost() {

    }

    @DeleteMapping("/posts/{id}")
    public void disablePost() {

    }

}
