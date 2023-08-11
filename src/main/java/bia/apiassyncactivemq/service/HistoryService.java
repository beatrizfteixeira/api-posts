package bia.apiassyncactivemq.service;


import bia.apiassyncactivemq.entity.History;
import bia.apiassyncactivemq.entity.HistoryEnum;
import bia.apiassyncactivemq.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class HistoryService {
    @Autowired
    private HistoryRepository historyRepository;

    public History saveHistory (History h) {
        return historyRepository.save(h);
    }
}
