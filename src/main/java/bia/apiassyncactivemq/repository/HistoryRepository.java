package bia.apiassyncactivemq.repository;

import bia.apiassyncactivemq.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History, Integer> {
}
