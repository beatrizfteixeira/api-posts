package bia.apiassyncactivemq.service;

import bia.apiassyncactivemq.entity.Post;
import bia.apiassyncactivemq.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;


@Service
public class RequestService {

    @Autowired
    private ProcessPostService processPostService;

    @Async
    public CompletableFuture<Void> processPost(Integer id) {
        CompletableFuture<Post> postFuture = processPostService.processPostAsync(id);
        return postFuture.thenAcceptAsync(post -> {
        }).exceptionally(ex -> {
           // ex.printStackTrace();
            throw new CustomException(ex.getMessage());
        });
    }

    @Async
    public CompletableFuture<List<Post>> getProcessedPosts() {
        List<Post> posts = processPostService.getPosts();
        return CompletableFuture.completedFuture(posts);
    }
    @Async
    public CompletableFuture<Void> disablePost(Integer id) {
        return CompletableFuture.runAsync(() -> {
            try {
                processPostService.disablePost(id);
            } catch (Exception e) {

               // e.printStackTrace();
                throw new CustomException(e.getMessage());
            }
        });
    }
    @Async
    public CompletableFuture<Void> reprocessPost(Integer id) {
        return CompletableFuture.runAsync(() -> {
            try {
                processPostService.reprocessPostAsync(id);
            } catch (Exception e) {
               // e.printStackTrace();
                throw new CustomException(e.getMessage());
            }
        });
    }


}
