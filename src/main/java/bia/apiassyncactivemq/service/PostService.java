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

    public HistoryEnum getLastState(Integer id) {
        Post post = findPost(id);
        List<History> list = post.getHistory();
        HistoryEnum lastStateEnum = list.get(list.size() - 1).getStatus();

        return lastStateEnum;
    }
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
    public Post findPost(Integer id) {
        return postRepository.findById(id).get();
    }
}

