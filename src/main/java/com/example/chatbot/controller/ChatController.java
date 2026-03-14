package com.example.chatbot.controller;

import com.example.chatbot.config.ChatbotProperties;
import com.example.chatbot.dto.ChatRequest;
import com.example.chatbot.dto.ChatResponse;
import com.example.chatbot.service.ChatService;
import com.example.chatbot.service.SafetyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final ChatService chatService;
    private final SafetyService safetyService;
    private final ChatbotProperties properties;

    public ChatController(ChatService chatService, SafetyService safetyService, ChatbotProperties properties) {
        this.chatService = chatService;
        this.safetyService = safetyService;
        this.properties = properties;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "ok", "model", properties.ollamaModel());
    }

    @PostMapping("/chat")
    public ChatResponse chat(@Valid @RequestBody ChatRequest request) {
        String message = request.message().trim();

        if (message.length() > properties.maxUserMessageChars()) {
            throw new ChatException("Message too long", HttpStatus.BAD_REQUEST);
        }
        if (!safetyService.isSafe(message)) {
            throw new ChatException("Prompt violates safety policy", HttpStatus.BAD_REQUEST);
        }

        String sessionId = chatService.resolveSessionId(request.sessionId());
        try {
            String reply = chatService.reply(sessionId, message);
            return new ChatResponse(reply, sessionId);
        } catch (Exception ex) {
            throw new ChatException(
                    "LLM backend unavailable. Ensure Ollama is running and model is pulled: " + properties.ollamaModel(),
                    HttpStatus.SERVICE_UNAVAILABLE
            );
        }
    }

    @ExceptionHandler(ChatException.class)
    public ResponseEntity<Map<String, String>> handleChatException(ChatException ex) {
        return ResponseEntity.status(ex.getStatus()).body(Map.of("detail", ex.getMessage()));
    }

    static class ChatException extends RuntimeException {
        private final HttpStatus status;

        ChatException(String message, HttpStatus status) {
            super(message);
            this.status = status;
        }

        public HttpStatus getStatus() {
            return status;
        }
    }
}
