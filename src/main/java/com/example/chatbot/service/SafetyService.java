package com.example.chatbot.service;

import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class SafetyService {

    private static final Set<String> BLOCKED_KEYWORDS = Set.of(
            "build a bomb",
            "credit card skimmer",
            "malware source code",
            "how to kill"
    );

    public boolean isSafe(String message) {
        String lowered = message.toLowerCase();
        return BLOCKED_KEYWORDS.stream().noneMatch(lowered::contains);
    }
}
