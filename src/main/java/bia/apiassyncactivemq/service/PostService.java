package bia.apiassyncactivemq.service;

import bia.apiassyncactivemq.entity.Comment;
import bia.apiassyncactivemq.entity.History;
import bia.apiassyncactivemq.entity.HistoryEnum;
import bia.apiassyncactivemq.entity.Post;
import bia.apiassyncactivemq.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    @Autowired
    private HistoryService historyService;
    @Autowired
    private PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
    public Post savePost(Post post) {
        return postRepository.save(post);
    }
    public Comment addCommentToPost(Integer postId, Comment comment) throws EntityNotFoundException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        comment.setPost(post);
        post.getComments().add(comment);

        return comment;
    }

//    public History addHistoryToPost(Integer postId, History history) throws EntityNotFoundException {
//        Post post = postRepository.findById(postId)
//                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
//
//        history.setPost(post);
//        post.getHistory().add(history);
//
//        return history;
//    }
public History addHistoryToPost(Integer postId, History history) {
    Post post = postRepository.findById(postId).get();

    history.setPost(post);
    post.getHistory().add(history);

    return history;
}

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
    public Post findPost(Integer id) {
        return postRepository.findById(id).get();
    }
}

