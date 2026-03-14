package com.example.chatbot.service;

import com.example.chatbot.model.ChatTurn;
import com.example.chatbot.repository.ChatTurnRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ChatService {

    private final ChatTurnRepository repository;
    private final OllamaClient ollamaClient;

    public ChatService(ChatTurnRepository repository, OllamaClient ollamaClient) {
        this.repository = repository;
        this.ollamaClient = ollamaClient;
    }

    public String resolveSessionId(String sessionId) {
        if (sessionId == null || sessionId.isBlank()) {
            return UUID.randomUUID().toString();
        }
        return sessionId;
    }

    public String reply(String sessionId, String message) {
        List<ChatTurn> turns = repository.findBySessionIdOrderByIdAsc(sessionId);
        List<Map<String, String>> history = turns.stream()
                .map(turn -> Map.of("role", turn.getRole(), "content", turn.getContent()))
                .toList();

        repository.save(new ChatTurn(sessionId, "user", message));

        String reply = ollamaClient.chat(message, history);
        repository.save(new ChatTurn(sessionId, "assistant", reply));

        return reply;
    }
}
