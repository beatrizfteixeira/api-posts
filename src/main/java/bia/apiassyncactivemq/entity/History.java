package bia.apiassyncactivemq.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime date;
    @Enumerated(EnumType.STRING)
    private HistoryEnum status;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @JsonIgnore
    private Post post;

    public History(HistoryEnum status) {
        this.date = LocalDateTime.now();
        this.status = status;
    }
    public History(Integer id, HistoryEnum status, Post p) {
        this.date = LocalDateTime.now();
        this.status = status;
        this.post = p;
        this.id = id;
    }
    public History(Integer id, HistoryEnum status) {
        this.date = LocalDateTime.now();
        this.status = status;
        this.id = id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setStatus(HistoryEnum status) {
        this.status = status;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    @Override
    public String toString() {
        return "History{" +
                "id=" + id +
                ", date=" + date +
                ", status=" + status +
                ", post=" + post +
                '}';
    }

    // getters, setters, etc.
}








//
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//public class History {
//    @Getter
//    @Id
//    private Integer id;
//    private LocalDateTime date;
//
//    @Enumerated(EnumType.STRING)
//    private HistoryEnum status;
//
//    @Override
//    public String toString() {
//        return "History{" +
//                "id=" + id +
//                ", date=" + date +
//                ", status=" + status +
//                ", post=" + post +
//                '}';
//    }
//
//    public History(Integer id, LocalDateTime date, HistoryEnum status) {
//        this.id = id;
//        this.date = date;
//        this.status = status;
//    }
//
//    public void setStatus(HistoryEnum status) {
//        this.status = status;
//    }
//    @ManyToOne
//    @JoinColumn(name = "post_id")
//    private Post post;
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public void setDate(LocalDateTime date) {
//        this.date = date;
//    }
//
//    public LocalDateTime getDate() {
//        return date;
//    }
//
//
//}
