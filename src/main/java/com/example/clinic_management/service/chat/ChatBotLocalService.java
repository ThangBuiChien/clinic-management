package com.example.clinic_management.service.chat;

import com.example.clinic_management.entities.ChatMessageEntity;

public interface ChatBotLocalService {

    public String getChatBotResponse(String question);

    public void processAndRespond(ChatMessageEntity userMessage, Long roomId);
}
