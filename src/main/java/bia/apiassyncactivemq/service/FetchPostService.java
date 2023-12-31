package bia.apiassyncactivemq.service;


import bia.apiassyncactivemq.entity.Comment;
import bia.apiassyncactivemq.entity.History;
import bia.apiassyncactivemq.entity.HistoryEnum;
import bia.apiassyncactivemq.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FetchPostService {
    @Autowired
    private PostService postService;
    @Autowired
    private HistoryService historyService;

    @Autowired
    private CommentService commentService;
    @Autowired
    private FeignService feignService;

public void getAndSavePost(Integer id) {
    // Criar um novo post e salvar no banco
    Post post = new Post(id);
    postService.savePost(post);

    // Criar e salvar o histórico de post CREATED
    History historyCreated = new History(HistoryEnum.CREATED);
    historyCreated.setPost(post);
    historyService.saveHistory(historyCreated);
    postService.savePost(post);

    // Criar e salvar o histórico de busca de POST
    History historyFind = new History(HistoryEnum.POST_FIND);
    historyFind.setPost(post);
    historyService.saveHistory(historyFind);

    // Buscar o post na API externa
    Post fetchedPost = feignService.fetchPost(id);

    // Atualizar o post com os dados da API externa e salvar
    post.setTitle(fetchedPost.getTitle());
    post.setBody(fetchedPost.getBody());
    postService.savePost(post);

    // Criar e salvar o histórico de post OK
    History historyPostOk = new History(HistoryEnum.POST_OK);
    historyPostOk.setPost(post);
    historyService.saveHistory(historyPostOk);

    // Buscar o comentários na API externa
    History historyCommentsFind = new History(HistoryEnum.COMMENTS_FIND);
    historyCommentsFind.setPost(post);
    historyService.saveHistory(historyCommentsFind);
    List<Comment> listComments = feignService.fetchComments(id);
    System.out.println(listComments.size());

    commentService.setPost(listComments, post);
    // Salvar comentários associados ao post
    post.setComments(listComments);
    postService.savePost(post);

    // Criar e salvar o histórico COMMENTS_OK
    History historyCommentsOk = new History(HistoryEnum.COMMENTS_OK);
    historyCommentsOk.setPost(post);
    historyService.saveHistory(historyCommentsOk);


    History historyEnabled = new History(HistoryEnum.ENABLED);
    historyEnabled.setPost(post);
    historyService.saveHistory(historyEnabled);
}
    public List<Post> getPosts() {
       return postService.getAllPosts();
    }

}
