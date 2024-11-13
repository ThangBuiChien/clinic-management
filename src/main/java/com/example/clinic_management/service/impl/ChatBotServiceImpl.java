package com.example.clinic_management.service.impl;

import com.example.clinic_management.config.ChatBotConfig;
import com.example.clinic_management.entities.ChatBotConversation;
import com.example.clinic_management.service.ChatBotConversationService;
import com.example.clinic_management.service.ChatBotService;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatBotServiceImpl implements ChatBotService {

    private final OpenAiService openAiService;
    private final ChatBotConfig chatBotConfig;
    private final ChatBotConversationService chatBotConversationService;

    @Override
    public String generateResponse(String prompt) {
        try {
            ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(), prompt);
            ChatCompletionRequest request = ChatCompletionRequest.builder()
                    .messages(List.of(userMessage))
                    .model("gpt-4o-mini")
                    .maxTokens(300)
                    .build();

            ChatCompletionResult response = openAiService.createChatCompletion(request);

            return response.getChoices().get(0).getMessage().getContent();
        } catch (Exception e) {
//            log.error("Error generating OpenAI response", e);
            return "I apologize, but I encountered an error processing your request.";
        }
    }

    @Override
    public String generateResponse(String sessionId, String userMessage) {
        try {
            ChatBotConversation conversation = chatBotConversationService.getOrCreateConversation(sessionId);

            // If this is a new conversation, add the system prompt
            if (conversation.getMessages().isEmpty()) {
                ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(),
                        chatBotConfig.getSystemPrompt());
                chatBotConversationService.addMessage(sessionId, systemMessage);
            }

            // Add user message
            ChatMessage userChatMessage = new ChatMessage(ChatMessageRole.USER.value(), userMessage);
            chatBotConversationService.addMessage(sessionId, userChatMessage);

            // Create completion request with conversation history
            ChatCompletionRequest request = ChatCompletionRequest.builder()
                    .messages(conversation.getMessages())
                    .model("gpt-4o-mini")
                    .maxTokens(300)
                    .temperature(0.7)
                    .build();

            ChatCompletionResult response = openAiService.createChatCompletion(request);
            String assistantResponse = response.getChoices().get(0).getMessage().getContent();

            // Add assistant's response to conversation history
            ChatMessage assistantMessage = new ChatMessage(ChatMessageRole.ASSISTANT.value(),
                    assistantResponse);
            chatBotConversationService.addMessage(sessionId, assistantMessage);

            return assistantResponse;
        } catch (Exception e) {
//            log.error("Error generating OpenAI response", e);
            return "I apologize, but I encountered an error processing your request.";
        }
    }
}
