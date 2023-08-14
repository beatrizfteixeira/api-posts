package bia.apiassyncactivemq.controller;


import bia.apiassyncactivemq.entity.Post;
import bia.apiassyncactivemq.service.RequestService;
import bia.apiassyncactivemq.service.ProcessPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.concurrent.CompletionException;

@RestController
public class Controller {
    @Autowired
    private ProcessPostService processPostService;

    @Autowired
    private RequestService requestService;

    private final JmsTemplate jmsTemplate;

    @Autowired
    public Controller(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }
    @PostMapping("/posts/{id}")
    public ResponseEntity<String> processPost(@PathVariable(name = "id") Integer id) {
        try {
            requestService.processPost(id).join();
            return ResponseEntity.ok("Processing post...");
        } catch (CompletionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(cause.getMessage());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
            }
        }
    }
    @GetMapping("/posts")
    public DeferredResult<ResponseEntity<List<Post>>> getProcessedPosts() {
        DeferredResult<ResponseEntity<List<Post>>> deferredResult = new DeferredResult<>();

        requestService.getProcessedPosts().whenComplete((posts, throwable) -> {
            if (throwable != null) {
                deferredResult.setErrorResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
            } else {
                deferredResult.setResult(ResponseEntity.ok(posts));
            }
        });
        return deferredResult;
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<String> reprocessPost(@PathVariable (name = "id") Integer id) {
        try {
            requestService.reprocessPost(id).join();
            return ResponseEntity.ok("Processing post...");
        } catch (CompletionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + cause.getMessage());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
            }
        }
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<String> disablePost(@PathVariable (name = "id") Integer id) {
        try {
            requestService.disablePost(id).join();
            return ResponseEntity.ok("Processing post...");
        } catch (CompletionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + cause.getMessage());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
            }
        }
    }

}
