package com.learn.mcp.controllers;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
public class AIController {

    private ChatClient chatClient;

    public AIController(ChatClient.Builder chatClientBuilder, ToolCallbackProvider toolCallbackProvider){
        this.chatClient = chatClientBuilder
                .defaultToolCallbacks(toolCallbackProvider)
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }


    @PostMapping("/chat")
    public ResponseEntity<String> getAiResponse(
            @RequestParam("q") String query
    ) {

        var content = chatClient.prompt().user(query).call().content();

        return ResponseEntity.ok(content);
    }

}
