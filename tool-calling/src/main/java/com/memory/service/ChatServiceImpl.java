package com.memory.service;

import com.memory.tools.DateTimeTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService{

    private ChatClient chatClient;

    public ChatServiceImpl(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    // Chat method::: get response from LLM model
    // chatClient::: client for calling LLM model
    // tool description ::: chatbot for calling tool
    // attach tool to chatClient
    @Override
    public String chat(String query) {
        return this.chatClient
                .prompt()
                .tools(new DateTimeTool())
                .user(query)
                .call()
                .content();
    }
}
