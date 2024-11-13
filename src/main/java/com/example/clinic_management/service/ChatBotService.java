package com.example.clinic_management.service;

public interface ChatBotService {
    public String generateResponse(String prompt);

    public String generateResponse(String sessionId, String userMessage);
}
