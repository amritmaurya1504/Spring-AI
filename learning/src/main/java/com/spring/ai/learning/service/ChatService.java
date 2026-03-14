package com.spring.ai.learning.service;

import com.spring.ai.learning.enitity.Tut;
import reactor.core.publisher.Flux;

import java.util.List;

public interface ChatService {
    List<Tut> chats(String message);
    String chat(String message);
    String chatTemplate();
    String testAdvisorConteptChat(String message);
    Flux<String> streamChat(String message);
}
