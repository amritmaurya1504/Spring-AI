package com.pwa.helpdesk.services.impl;

import com.pwa.helpdesk.services.AiService;
import com.pwa.helpdesk.tools.CurrentSystemTime;
import com.pwa.helpdesk.tools.EmailTool;
import com.pwa.helpdesk.tools.TicketDatabaseTool;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Getter
@Setter
public class AiServiceImpl implements AiService {

    private final ChatClient chatClient;
    private final TicketDatabaseTool ticketDatabaseTool;
    private final CurrentSystemTime currentSystemTime;
    private final EmailTool emailTool;

    @Value(("classpath:/helpdesk-system.st"))
    private Resource systemPromptResource;

    @Override
    public String getResponseFromAssistant(String query, String conversationId) {
        return chatClient
                .prompt()
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, conversationId))
                .tools(ticketDatabaseTool, currentSystemTime, emailTool)
                .system(systemPromptResource)
                .user(query)
                .call()
                .content();
    }

    @Override
    public Flux<String> streamResponseFromAssistant(String query, String conversationId) {
        return chatClient
                .prompt()
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, conversationId))
                .tools(ticketDatabaseTool, currentSystemTime, emailTool)
                .system(systemPromptResource)
                .user(query)
                .stream()
                .content();
    }
}
