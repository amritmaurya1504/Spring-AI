package com.pwa.helpdesk.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AiService {

    String getResponseFromAssistant(String query, String conversationId);
    Flux<String> streamResponseFromAssistant(String query, String conversationId);

}
