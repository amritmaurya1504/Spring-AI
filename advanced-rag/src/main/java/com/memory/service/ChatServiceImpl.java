package com.memory.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.TranslationQueryTransformer;
import org.springframework.ai.rag.retrieval.join.ConcatenationDocumentJoiner;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.print.Doc;
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
    public String getResponse(String userQuery) {

        // Build Retrieval Augmentation pipeline (RAG pipeline)
        var advisors = RetrievalAugmentationAdvisor.builder()

                // Step 1: Transform the query before retrieval
                .queryTransformers(

                        // Rewrites the query to improve semantic meaning
                        RewriteQueryTransformer.builder()
                                .chatClientBuilder(chatClient.mutate().clone())
                                .build(),

                        // Translates query into English (useful for multilingual input)
                        TranslationQueryTransformer.builder()
                                .targetLanguage("urdu")
                                .chatClientBuilder(chatClient.mutate().clone())
                                .build()
                )

                // Step 2: Expand the query into multiple variations
                // Helps in retrieving better/more relevant documents
                .queryExpander(
                        MultiQueryExpander.builder()
                                .chatClientBuilder(chatClient.mutate().clone())
                                .build()
                )

                // Step 3: Retrieve documents from vector database
                .documentRetriever(
                        VectorStoreDocumentRetriever.builder()
                                .vectorStore(vectorStore)          // Your vector DB
                                .topK(3)                           // Fetch top 3 similar docs
                                .similarityThreshold(0.5)          // Minimum similarity score
                                .build()
                )

                // Step 4: Combine retrieved documents into a single context
                .documentJoiner(
                        new ConcatenationDocumentJoiner()
                )

                // Step 5: Augment the original query with retrieved context
                .queryAugmenter(
                        ContextualQueryAugmenter.builder().build()
                )

                // Build the full advisor pipeline
                .build();

        // Step 6: Send final augmented query to LLM
        return this.chatClient
                .prompt()
                .advisors(advisors)     // Attach Advance RAG pipeline
                .user(userQuery)        // Original user input
                .call()                 // Execute request
                .content();             // Get response text
    }

    @Override
    public void saveData(List<Document> list) {
        this.vectorStore.add(list);
    }
}
