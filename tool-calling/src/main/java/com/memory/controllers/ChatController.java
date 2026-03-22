package com.memory.controllers;

import com.memory.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class ChatController {

    private ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping(value = "/chat")
    public ResponseEntity<String> chat(
            @RequestParam(value = "q") String query
    ) {
        return ResponseEntity.ok(chatService.chat(query));
    }

}
