package com.example.clinic_management.service.chat;

import com.example.clinic_management.entities.ChatMessageEntity;

public interface ChatBotService {
    public String generateResponse(String prompt);

    public String generateResponse(String sessionId, String userMessage);

    public void processAndRespond(ChatMessageEntity userMessage, Long roomId);
}
