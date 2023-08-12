package bia.apiassyncactivemq.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    private Integer id;
    private String title;

    private String body;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<History> history = new ArrayList<>();
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }


    public void addHistory(History h) {
        history.add(h);
    }
    public Post(List<History> history){
        this.history = history;
    }
    public Post(Integer id){
        this.id = id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public List<History> getHistory() {
        return history;
    }

//    public History getLastHistory() {
//        return history.get(history.size() - 1);
//    }
    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
