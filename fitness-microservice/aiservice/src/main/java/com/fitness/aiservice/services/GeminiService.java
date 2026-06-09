package com.fitness.aiservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service

public class GeminiService {
    private final WebClient webClient;
    @Value("${gemini.api.url}")
    private String geminiApiUri;
    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public GeminiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public String getAnswer(String question){
        Map<String,Object> requestBody = Map.of("contents",new Object[]{
                Map.of("parts",new Object[]{
                        Map.of("text",question)
                })
        });
//        String response=webClient.post().uri(geminiApiUri+geminiApiKey)
//                .header("Content-Type","application/json")
//                .bodyValue(requestBody)
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
//        return response;
        try {
            return webClient.post()
                    .uri(geminiApiUri + geminiApiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
