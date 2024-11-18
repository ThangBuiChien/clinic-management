package com.example.clinic_management.entities;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.theokanning.openai.completion.chat.ChatMessage;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatBotConversation {

    private String sessionId;
    private List<ChatMessage> messages;
    private LocalDateTime lastUpdated;
    private Map<String, String> metadata;
}
