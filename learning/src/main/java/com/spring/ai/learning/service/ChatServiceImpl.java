package com.spring.ai.learning.service;

import com.spring.ai.learning.advisors.TokenPrint;
import com.spring.ai.learning.enitity.Tut;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@Service

public class ChatServiceImpl implements ChatService {

    private ChatClient chatClient;

    @Value("classpath:/prompts/user-messate.st")
    private Resource userMessage;

    @Value("classpath:/prompts/system-message.st")
    private Resource systemMessage;

    public ChatServiceImpl(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("gpt-3.5-turbo")
                        .temperature(0.3)
                        .maxTokens(1000)
                        .build())
                .build();

        // Default options can also be set at the prompt level, which will override the default options set at the client level.
    }

    @Override
    public List<Tut> chats(String message) {

        /*String prompt = "tell me about virat kohli";*/
        Prompt prompt1 = new Prompt(message);
        // 1. Call the LLM for response
        /*var content = chatClient.prompt()
                .user(prompt)
                .system("As an expert in cricket.")
                .call()
                .content();*/

        /*String content = chatClient.prompt(prompt1)
                .call()
                .chatResponse()
                .getResult().
                getOutput()
                .getText();*/

       /* var content = chatClient.prompt(prompt1)
                .call().chatResponse().getMetadata();

        System.out.println(content);*/

        List<Tut> tut = chatClient.prompt(prompt1)
                .call()
                .entity(new ParameterizedTypeReference<List<Tut>>() {
                });

        return tut;
    }

    @Override
    public String chat(String message) {
        /*Prompt prompt = new Prompt(message, OpenAiChatOptions.builder()
                .model("gpt-3.5-turbo")
                .temperature(0.3)
                .maxTokens(100)
                .build());*/
        Prompt prompt = new Prompt(message);

        // Modify this prompt and add extra instructions to the LLM to get a more specific response.
        String queryStr = "As an expert in coding and programming. Always write programs in java. " +
                "Now reply to the following query: ${query}";

        // Now we use PromptTemplate to create a prompt with the query string and the options.


        var content = chatClient
                .prompt()
                .user(promptUserSpec -> promptUserSpec.text(queryStr).param("query", message))
                .call()
                .content();

        return content;
    }

    @Override
    public String chatTemplate(){

        // 1. Prompt Template is a way to create a prompt with variables that can be replaced with actual values at runtime. It is a way to create a prompt with a specific structure and format.
        PromptTemplate strTemplate = PromptTemplate.builder()
                .template("What is {techName}? tell me also about {techExample}")
                .build();

        // 1a. System Prompt
        var systemPrompt = SystemPromptTemplate.builder()
                .template("You are an expert in {techName}.")
                .build();

        Message systemPromptRendered = systemPrompt.createMessage(Map.of(
            "techName", "Spring"
        ));

        // 2. Render the template with actual values for the variables.
        Message userPrompt = strTemplate.createMessage(Map.of(
            "techName", "Spring",
            "techExample", "Spring Exceptions"
        ));

        // 3. Create the prompt with the rendered template.
        Prompt prompt = new Prompt(systemPromptRendered, userPrompt);

        // 4. Call the LLM for response

        /*return chatClient.prompt(prompt)
                .call()
                .content();*/

        return this.chatClient
                .prompt()
                .user(userMessage -> userMessage.text(this.userMessage).param("concept", "Spring Framework"))
                .call()
                .content();
    }

    @Override
    public String testAdvisorConteptChat(String message) {
        return this.chatClient
                .prompt()
                .advisors(new TokenPrint(), new SafeGuardAdvisor(List.of("sex", "nudity"))) // you can define at the time of building ChatClient or at the time of calling prompt. Advisor will be executed in the order they are defined.
                .system(systemMessage -> systemMessage.text(this.systemMessage))
                .user(userMessage -> userMessage.text(this.userMessage).param("concept", message))
                .call()
                .content();
    }

    @Override
    public Flux<String> streamChat(String message) {
        return this.chatClient
                .prompt()
                .system(systemMessage -> systemMessage.text(this.systemMessage))
                .user(userMessage -> userMessage.text(this.userMessage).param("concept", message))
                .stream()
                .content();
    }

}
