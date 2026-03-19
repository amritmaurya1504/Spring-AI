package com.memory.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service

public class ChatServiceImpl implements ChatService {

    private Logger log = LoggerFactory.getLogger(ChatServiceImpl.class);

    private ChatClient chatClient;

    @Value("classpath:/prompts/user-message.st")
    private Resource userMessage;

    @Value("classpath:/prompts/system-message.st")
    private Resource systemMessage;

    private VectorStore vectorStore;

    public ChatServiceImpl(ChatClient chatClient, VectorStore vectorStore) {
        this.vectorStore = vectorStore;
        this.chatClient = chatClient;
    }

    @Override
    public String chat(String query, String userId) {
        return this.chatClient
                .prompt()
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, userId))
                .user(userMessage -> userMessage.text(this.userMessage).params(Map.of("message", query)))
                .call()
                .content();
    }

    // 1 RAG Implementation
    @Override
    public String chatTemplate(String query, String userId) {

        // Step 1: Create a search request for the vector database
        // topK(3) -> retrieve top 3 most similar documents
        // similarityThreshold(0.6) -> only return results with similarity >= 0.6
        // query(query) -> user input that will be converted into an embedding and searched
        SearchRequest searchRequest = SearchRequest.builder()
                .topK(3)
                .similarityThreshold(0.6)
                .query(query)
                .build();
        // Step 2: Perform similarity search in the vector store
        // This returns a list of documents that are semantically similar to the query
        List<Document> documents = this.vectorStore.similaritySearch(searchRequest);
        // Step 3: Extract only the text content from the retrieved documents
        List<String> documentList = documents.stream().map(Document::getText).toList();
        // Step 4: Combine all retrieved document texts into a single context string
        // This context will be passed to the LLM so it can answer based on retrieved knowledge
        String contextFromVectorDB = String.join(",", documentList);
        log.info("Context Data: {}", contextFromVectorDB);

        // Step 5: Send prompt to the AI model using ChatClient
        return this.chatClient
                .prompt()
                .system(systemMessage -> systemMessage.text(this.systemMessage).param("documents", contextFromVectorDB))
                .user(userMessage -> userMessage.text(this.userMessage).params(Map.of("query", query)))
                .call()
                .content();
    }

    // 2 RAG Implementation
    @Override
    public String chatTemplate2(String query, String userId) {

        // No need to manually get context data and attach to the system prompt we can do it using QuestionAnswerAdvisors

        return this.chatClient
                .prompt()
                .advisors(QuestionAnswerAdvisor.builder(vectorStore)
                        .searchRequest(
                                SearchRequest.builder()
                                        .topK(3)
                                        .similarityThreshold(0.5)
                                        .build()
                        )
                        .build())
                .user(user -> user.text(this.userMessage).param("query", query))
                .call()
                .content();
    }

    // 3 RAG Implementation
    @Override
    public String chatTemplate3(String query, String userId) {

        var advisors = RetrievalAugmentationAdvisor.builder()
                .documentRetriever(VectorStoreDocumentRetriever
                        .builder()
                        .topK(3)
                        .vectorStore(this.vectorStore)
                        .similarityThreshold(0.5)
                        .build())
                .queryAugmenter(ContextualQueryAugmenter.builder()
                        .allowEmptyContext(true)
                        .build())
                .build();

        return this.chatClient
                .prompt()
                .advisors(advisors)
                .user(user -> user.text(this.userMessage).param("query", query))
                .call()
                .content();
    }

    @Override
    public void saveData(List<String> list) {
        List<Document> documentList = list.stream().map(Document::new).toList();
        this.vectorStore.add(documentList);
    }
}
