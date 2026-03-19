package com.memory.service;

import java.util.List;

public interface ChatService {

    String chat(String query, String userId);
    void saveData(List<String> list);
    String chatTemplate(String query, String userId);
    String chatTemplate2(String query, String userId);
    String chatTemplate3(String query, String userId);
}
