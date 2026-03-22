package com.memory.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class WeatherTool {

    private RestClient restClient;

    @Value("${app.weather.api-key}")
    private String weatherAPIKey;

    public WeatherTool(RestClient restClient) {
        this.restClient = restClient;
    }

    @Tool(description = "Get weather information of the given city.")
    public String getWeather(@ToolParam(description = "City of which we want to get weather information") String city){
        //1. External API to get current weather information
        var response = restClient
                .get()
                .uri(
                        uriBuilder -> uriBuilder.path("/current.json")
                                .queryParam("key", weatherAPIKey)
                                .queryParam("q", city)
                                .build()
                )
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {});


        return response != null ? response.toString() : null;
    }

}
