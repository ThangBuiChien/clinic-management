package com.example.clinic_management.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.clinic_management.entities.ChatBotConversation;
import com.example.clinic_management.service.chat.ChatBotConversationService;
import com.theokanning.openai.completion.chat.ChatMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatBotConversationServiceImpl implements ChatBotConversationService {

    private final Map<String, ChatBotConversation> conversations = new ConcurrentHashMap<>();
    private final int MAX_CONVERSATION_AGE_HOURS = 24;
    private final int MAX_MESSAGES_PER_CONVERSATION = 10;

    @Override
    public ChatBotConversation getOrCreateConversation(String sessionId) {
        return conversations.computeIfAbsent(sessionId, id -> ChatBotConversation.builder()
                .sessionId(id)
                .messages(new ArrayList<>())
                .lastUpdated(LocalDateTime.now())
                .metadata(new HashMap<>())
                .build());
    }

    @Override
    public void addMessage(String sessionId, ChatMessage message) {

        ChatBotConversation conversation = getOrCreateConversation(sessionId);
        conversation.getMessages().add(message);
        conversation.setLastUpdated(LocalDateTime.now());

        // Trim conversation if it gets too long
        if (conversation.getMessages().size() > MAX_MESSAGES_PER_CONVERSATION) {
            // Keep system message and most recent messages
            List<ChatMessage> trimmedMessages = new ArrayList<>();
            trimmedMessages.add(conversation.getMessages().get(0)); // System message
            trimmedMessages.addAll(conversation
                    .getMessages()
                    .subList(
                            conversation.getMessages().size() - MAX_MESSAGES_PER_CONVERSATION + 1,
                            conversation.getMessages().size()));
            conversation.setMessages(trimmedMessages);
        }
    }

    @Scheduled(fixedRate = 3600000) // Clean up every hour
    @Override
    public void cleanupOldConversations() {
        LocalDateTime cutoff = LocalDateTime.now().minusHours(MAX_CONVERSATION_AGE_HOURS);
        conversations
                .entrySet()
                .removeIf(entry -> entry.getValue().getLastUpdated().isBefore(cutoff));
    }
}
