package com.memory.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class AiConfig {

// @Bean is not a class — it is an annotation that tells Spring to register the returned object of that method as a bean in
// the Spring container.

    Logger log = LoggerFactory.getLogger(AiConfig.class);


    @Bean
    public ChatClient openAIChatClient(ChatClient.Builder builder, ChatMemory chatMemory){
        log.info("ChatMemory bean: {}", chatMemory.getClass().getSimpleName());

        return builder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("gpt-4o-mini")
                        .temperature(0.3)
                        .maxTokens(200)
                        .build()
                ).build();
    }

    @Bean
    public RestClient restClient(){
        return RestClient
                .builder()
                .baseUrl("http://api.weatherapi.com/v1")
                .build();
    }
}
