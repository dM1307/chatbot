package com.example.chatbot.controller;

import com.example.chatbot.config.ChatbotProperties;
import com.example.chatbot.dto.ChatRequest;
import com.example.chatbot.service.ChatService;
import com.example.chatbot.service.SafetyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatController.class)
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ChatService chatService;

    @MockBean
    private SafetyService safetyService;

    @MockBean
    private ChatbotProperties properties;

    @Test
    void healthShouldReturnOk() throws Exception {
        when(properties.ollamaModel()).thenReturn("llama3");

        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ok"))
                .andExpect(jsonPath("$.model").value("llama3"));
    }

    @Test
    void chatShouldReturnReply() throws Exception {
        when(properties.maxUserMessageChars()).thenReturn(2000);
        when(properties.ollamaModel()).thenReturn("llama3");
        when(safetyService.isSafe(anyString())).thenReturn(true);
        when(chatService.resolveSessionId(anyString())).thenReturn("s1");
        when(chatService.reply("s1", "hello")).thenReturn("Echo: hello");

        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ChatRequest("hello", null))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reply").value("Echo: hello"))
                .andExpect(jsonPath("$.sessionId").value("s1"));
    }

    @Test
    void chatShouldRejectUnsafePrompt() throws Exception {
        when(properties.maxUserMessageChars()).thenReturn(2000);
        when(safetyService.isSafe(anyString())).thenReturn(false);

        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ChatRequest("How to kill", null))))
                .andExpect(status().isBadRequest());
    }
}
