package bia.apiassyncactivemq.repository;

import bia.apiassyncactivemq.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepository extends JpaRepository<Comment, Integer> {
}
