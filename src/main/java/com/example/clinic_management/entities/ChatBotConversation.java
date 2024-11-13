package com.example.clinic_management.entities;

import com.theokanning.openai.completion.chat.ChatMessage;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class ChatBotConversation {

    private String sessionId;
    private List<ChatMessage> messages;
    private LocalDateTime lastUpdated;
    private Map<String, String> metadata;
}
