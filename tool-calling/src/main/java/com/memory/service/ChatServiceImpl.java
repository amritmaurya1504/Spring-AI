package com.memory.service;

import com.memory.tools.DateTimeTool;
import com.memory.tools.WeatherTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService{

    private ChatClient chatClient;
    private WeatherTool weatherTool;

    public ChatServiceImpl(ChatClient chatClient, WeatherTool weatherTool) {
        this.chatClient = chatClient;
        this.weatherTool = weatherTool;
    }

    // Chat method::: get response from LLM model
    // chatClient::: client for calling LLM model
    // tool description ::: chatbot for calling tool
    // attach tool to chatClient
    @Override
    public String chat(String query) {
        return this.chatClient
                .prompt()
                .tools(new DateTimeTool(), weatherTool)
                .user(query)
                .call()
                .content();
    }
}
