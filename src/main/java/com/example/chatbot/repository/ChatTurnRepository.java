package com.example.chatbot.repository;

import com.example.chatbot.model.ChatTurn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatTurnRepository extends JpaRepository<ChatTurn, Long> {
    List<ChatTurn> findBySessionIdOrderByIdAsc(String sessionId);
}
