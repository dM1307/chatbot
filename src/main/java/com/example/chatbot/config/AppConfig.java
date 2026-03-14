package com.example.chatbot.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(ChatbotProperties.class)
public class AppConfig {

    @Bean
    RestClient restClient(ChatbotProperties properties) {
        return RestClient.builder()
                .baseUrl(properties.ollamaBaseUrl())
                .build();
    }
}
