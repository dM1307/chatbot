package com.example.chatbot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "chatbot")
public record ChatbotProperties(
        String appName,
        String ollamaBaseUrl,
        String ollamaModel,
        int maxUserMessageChars
) {
}
