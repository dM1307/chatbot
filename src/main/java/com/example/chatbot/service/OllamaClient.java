package com.example.chatbot.service;

import com.example.chatbot.config.ChatbotProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class OllamaClient {

    private static final String SYSTEM_PROMPT = "You are a helpful, practical AI assistant. Give clear, concise answers with steps and examples when useful.";

    private final RestClient restClient;
    private final ChatbotProperties properties;

    public OllamaClient(RestClient restClient, ChatbotProperties properties) {
        this.restClient = restClient;
        this.properties = properties;
    }

    @SuppressWarnings("unchecked")
    public String chat(String userMessage, List<Map<String, String>> history) {
        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", SYSTEM_PROMPT));

        if (history != null && !history.isEmpty()) {
            int start = Math.max(0, history.size() - 8);
            messages.addAll(history.subList(start, history.size()));
        }

        messages.add(Map.of("role", "user", "content", userMessage));

        Map<String, Object> payload = Map.of(
                "model", properties.ollamaModel(),
                "stream", false,
                "messages", messages,
                "options", Map.of("temperature", 0.4, "num_ctx", 2048)
        );

        Map<String, Object> response = restClient.post()
                .uri("/api/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload)
                .retrieve()
                .body(Map.class);

        if (response == null || !response.containsKey("message")) {
            throw new IllegalStateException("Invalid response from Ollama");
        }

        Object messageObj = response.get("message");
        if (!(messageObj instanceof Map<?, ?> messageMap) || !messageMap.containsKey("content")) {
            throw new IllegalStateException("Ollama response missing content");
        }

        return String.valueOf(messageMap.get("content")).trim();
    }
}
