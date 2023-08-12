package bia.apiassyncactivemq.service;


import bia.apiassyncactivemq.entity.History;
import bia.apiassyncactivemq.entity.HistoryEnum;
import bia.apiassyncactivemq.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DisablePostService {

    @Autowired
    private PostService postService;
    @Autowired
    private HistoryService historyService;

    public void disablePost(Integer id) {
        HistoryEnum h = postService.getLastState(id);
        Post post = postService.findPost(id);
        if (h == HistoryEnum.ENABLED) {
            System.out.println("Disabling post");
            History historyDisabled = new History(HistoryEnum.DISABLED);
            historyDisabled.setPost(post);
            postService.savePost(post);
            historyService.saveHistory(historyDisabled);
        }

    }


}


