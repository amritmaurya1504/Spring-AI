package com.memory.config;

import com.memory.advisors.TokenPrint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
//import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

// @Bean is not a class — it is an annotation that tells Spring to register the returned object of that method as a bean in
// the Spring container.

    Logger log = LoggerFactory.getLogger(AiConfig.class);


    @Bean
    public ChatClient openAIChatClient(ChatClient.Builder builder, ChatMemory chatMemory){
        log.info("ChatMemory bean: {}", chatMemory.getClass().getSimpleName());

        return builder
                .defaultAdvisors(new TokenPrint())
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("gpt-4o-mini")
                        .temperature(0.3)
                        .maxTokens(200)
                        .build()
                ).build();
    }
}
