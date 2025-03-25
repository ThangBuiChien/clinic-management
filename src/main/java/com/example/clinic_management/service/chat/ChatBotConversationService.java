package com.example.clinic_management.service.chat;

import com.example.clinic_management.entities.ChatBotConversation;
import com.theokanning.openai.completion.chat.ChatMessage;

public interface ChatBotConversationService {

    public ChatBotConversation getOrCreateConversation(String sessionId);

    public void addMessage(String sessionId, ChatMessage message);

    public void cleanupOldConversations();
}
