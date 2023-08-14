package bia.apiassyncactivemq.service;


import bia.apiassyncactivemq.entity.Comment;
import bia.apiassyncactivemq.entity.History;
import bia.apiassyncactivemq.entity.HistoryEnum;
import bia.apiassyncactivemq.entity.Post;
import bia.apiassyncactivemq.exception.InvalidPostStateException;
import bia.apiassyncactivemq.exception.PostAlreadyExistsException;
import bia.apiassyncactivemq.exception.PostNotFoundException;
import bia.apiassyncactivemq.exception.ResourceNotFoundInExternalApiException;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class ProcessPostService {
    @Autowired
    private PostService postService;
    @Autowired
    private HistoryService historyService;

    @Autowired
    private CommentService commentService;
    @Autowired
    private FeignService feignService;


    @Async
    public CompletableFuture<Post> processPostAsync(Integer id) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return processPost(id);
            } catch (RuntimeException e) {
               // e.printStackTrace();
                throw e;
            }
        });
    }

    @Async
    public CompletableFuture<Post> reprocessPostAsync(Integer id) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return reprocessPost(id);
            } catch (RuntimeException e) {
               // e.printStackTrace();
                throw e;
            }
        });
    }

    private Post processPost(Integer id) {
        boolean postExist = postService.verifyIfExists(id);
        if (postExist) {
            throw new PostAlreadyExistsException("Post with ID " + id + " already exists and cannot be POST again.");
        }
        Post post = getAndSavePost(id);
        getAndSaveComments(post, id);
        enablePost(post, id);
        return post;
    }
    private Post getAndSavePost(Integer id) {
        Post fetchedPost = null;
        // Criar um novo post e salvar no banco
        Post post = new Post(id);
        postService.savePost(post);

        // Criar e salvar o histórico de post CREATED
        History historyCreated = new History(HistoryEnum.CREATED);
        historyCreated.setPost(post);
        historyService.saveHistory(historyCreated);
        postService.savePost(post);

        // Criar e salvar o histórico de processo de busca de POST
        History historyFind = new History(HistoryEnum.POST_FIND);
        historyFind.setPost(post);
        historyService.saveHistory(historyFind);

        // Buscar o post na API externa
        try {
            fetchedPost = feignService.fetchPost(id);
        } catch (FeignException e) {
            // Criar e salvar o histórico de post FAILED
            History historyFailed = new History(HistoryEnum.FAILED);
            historyFailed.setPost(post);
            historyService.saveHistory(historyFailed);
            postService.savePost(post);

            History historyDisabled = new History(HistoryEnum.DISABLED);
            historyDisabled.setPost(post);
            postService.savePost(post);
            historyService.saveHistory(historyDisabled);

            throw new ResourceNotFoundInExternalApiException("JSONPlaceHolder API does not have a Post with given ID: " + id);
        }

        // Atualizar o post com os dados da API externa e salvar
        post.setTitle(fetchedPost.getTitle());
        post.setBody(fetchedPost.getBody());
        postService.savePost(post);

        // Criar e salvar o histórico de post OK
        History historyPostOk = new History(HistoryEnum.POST_OK);
        historyPostOk.setPost(post);
        historyService.saveHistory(historyPostOk);

        return post;
    }

    private void getAndSaveComments(Post post, Integer id) {
        // Buscar o comentários na API externa
        History historyCommentsFind = new History(HistoryEnum.COMMENTS_FIND);
        historyCommentsFind.setPost(post);
        historyService.saveHistory(historyCommentsFind);

        List<Comment> listComments = feignService.fetchComments(id);
        if (listComments == null) {
            throw new ResourceNotFoundInExternalApiException("JSONPlaceHolder API can't retrieve comments with given ID: " + id);
        }

        commentService.setPost(listComments, post);
        // Salvar comentários associados ao post
        post.setComments(listComments);
        postService.savePost(post);

        // Criar e salvar o histórico COMMENTS_OK
        History historyCommentsOk = new History(HistoryEnum.COMMENTS_OK);
        historyCommentsOk.setPost(post);
        historyService.saveHistory(historyCommentsOk);

    }

    private void enablePost(Post post, Integer id) {
        History historyEnabled = new History(HistoryEnum.ENABLED);
        historyEnabled.setPost(post);
        historyService.saveHistory(historyEnabled);
    }

    public Post reprocessPost(Integer id) { // possiveis erros: não encontrou o post,
        HistoryEnum h = null;
        Post post = null;
        boolean postExists = postService.verifyIfExists(id);
        if (postExists){
            h = postService.getLastState(id);
        } else {
            throw new PostNotFoundException("Post not found with id: " + id);
        }
        if (h == HistoryEnum.ENABLED || h == HistoryEnum.DISABLED) {
            post = postService.findPost(id);
            // Set UPDATING status
            History historyUpdating = new History(HistoryEnum.UPDATING);
            historyUpdating.setPost(post);
            postService.savePost(post);
            historyService.saveHistory(historyUpdating);

            // UPDATING post
            Post fetchedPost =  getAndSavePost(id);
            getAndSaveComments(post, id);
            enablePost(post, id);
        }
    else {
        throw new InvalidPostStateException("Cannot update post with ID " + id + " in its current state: " + h);
    }
    return post;
    }
    public void disablePost(Integer id) {
        HistoryEnum h = null;
        Post post = null;

        boolean postExists = postService.verifyIfExists(id);
        if (postExists){
            h = postService.getLastState(id);
        } else {
            throw new PostNotFoundException("Post not found with id: " + id);
        } if (h == HistoryEnum.ENABLED) {
            post = postService.findPost(id);
            History historyDisabled = new History(HistoryEnum.DISABLED);
            historyDisabled.setPost(post);
            postService.savePost(post);
            historyService.saveHistory(historyDisabled);
        } else {
            throw new InvalidPostStateException("Cannot DISABLE post with ID " + id + " in its current state: " + h);
        }
    }
    public List<Post> getPosts() {
       return postService.getAllPosts();
    }

}
