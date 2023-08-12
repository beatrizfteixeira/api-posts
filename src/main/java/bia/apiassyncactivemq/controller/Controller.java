package bia.apiassyncactivemq.controller;


import bia.apiassyncactivemq.entity.Post;
import bia.apiassyncactivemq.service.DisablePostService;
import bia.apiassyncactivemq.service.ProcessPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//endpoints expostos

@RestController
public class Controller {
    @Autowired
    private ProcessPostService processPostService;

    @Autowired
    private DisablePostService disablePostService;

    @PostMapping("/posts/{id}")
    public ResponseEntity<String> processPost(@PathVariable (name = "id") Integer id) {
        processPostService.processPost(id);
        return ResponseEntity.ok("Message sent to RabbitMQ...");
    }
    @GetMapping("/posts")
    @ResponseBody
    public List<Post> queryPosts() {
        List<Post> list = processPostService.getPosts();
        return list; // Retorna a lista de posts
    }


    @PutMapping("/posts/{id}")
    public void reprocessPost(@PathVariable (name = "id") Integer id) {
        processPostService.reprocessPost(id);
    }

    @DeleteMapping("/posts/{id}")
    public void disablePost(@PathVariable (name = "id") Integer id) {
        disablePostService.disablePost(id);
    }

}
