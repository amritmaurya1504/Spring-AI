package com.spring.ai.learning.controllers;

import com.spring.ai.learning.enitity.Tut;
import com.spring.ai.learning.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping
public class ChatController {

    private ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping(value = "/chats")
    public ResponseEntity<List<Tut>> chats(
            @RequestParam(value = "q") String query
    ) {
        return ResponseEntity.ok(chatService.chats(query));
    }

    @GetMapping(value = "/chat")
    public ResponseEntity<String> chat(
            @RequestParam(value = "q") String query
    ) {
        return ResponseEntity.ok(chatService.testAdvisorConteptChat(query));
    }

    @GetMapping("/stream-chat")
    public ResponseEntity<Flux<String>> streamChat(
            @RequestParam(value = "q") String query
    ) {
        return ResponseEntity.ok(chatService.streamChat(query));
    }


}
