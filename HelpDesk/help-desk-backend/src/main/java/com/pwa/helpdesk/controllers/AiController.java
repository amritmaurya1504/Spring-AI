package com.pwa.helpdesk.controllers;


import com.pwa.helpdesk.services.AiService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
@Getter
@Setter
@CrossOrigin("http://localhost:5173")
public class AiController {

    private final AiService aiService;

    @PostMapping("/chat")
    public ResponseEntity<String> getResponse(@RequestBody String query, @RequestHeader("ConversationId") String conversationId) {
        return ResponseEntity.ok(aiService.getResponseFromAssistant(query, conversationId));
    }

    @PostMapping("/stream/chat")
    public ResponseEntity<Flux<String>> streamResponse(@RequestBody String query, @RequestHeader("ConversationId") String conversationId) {
        return ResponseEntity.ok(aiService.streamResponseFromAssistant(query, conversationId));
    }

}
