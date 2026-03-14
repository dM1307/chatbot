package com.example.chatbot.controller;

import com.example.chatbot.config.ChatbotProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    private final ChatbotProperties properties;

    public ViewController(ChatbotProperties properties) {
        this.properties = properties;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("appName", properties.appName());
        return "index";
    }
}
