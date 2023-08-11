package bia.apiassyncactivemq.service;


import bia.apiassyncactivemq.entity.Comment;
import bia.apiassyncactivemq.entity.Post;
import bia.apiassyncactivemq.repository.CommentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentsRepository commentsRepository;


    public void saveComments(List<Comment> commentList) {
        for (Comment c : commentList) {
            commentsRepository.save(c);
        }
    }
    public void setPost(List<Comment> commentList, Post p) {
        for (Comment c : commentList) {
            c.setPost(p);
        }
    }
}
