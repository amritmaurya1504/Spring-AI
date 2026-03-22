package com.memory.service;

import org.springframework.ai.document.Document;

import java.util.List;

public interface ChatService {

    String getResponse(String userQuery);
    void saveData(List<Document> list);

}
