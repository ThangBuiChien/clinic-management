package com.example.clinic_management.service.impl;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.example.clinic_management.config.ChatBotConfig;
import com.example.clinic_management.dtos.requests.ChatMessageEntityRequestDTO;
import com.example.clinic_management.entities.ChatBotConversation;
import com.example.clinic_management.entities.ChatMessageEntity;
import com.example.clinic_management.entities.ChatRoom;
import com.example.clinic_management.enums.MessageType;
import com.example.clinic_management.exception.ResourceNotFoundException;
import com.example.clinic_management.repository.ChatRoomRepository;
import com.example.clinic_management.service.chat.ChatBotConversationService;
import com.example.clinic_management.service.chat.ChatBotService;
import com.example.clinic_management.service.chat.ChatRoomService;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatBotServiceImpl implements ChatBotService {

    private final OpenAiService openAiService;
    private final ChatBotConfig chatBotConfig;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatBotConversationService chatBotConversationService;
    private final ChatRoomService chatRoomService;
    private final ChatRoomRepository chatRoomRepository;

    private static final Logger logger = LoggerFactory.getLogger(ChatBotServiceImpl.class);

    private static final String BOT_NAME = "ClinicBot";
    private final Map<Long, List<ChatMessageEntity>> conversationHistory = new ConcurrentHashMap<>();

    @Value("${openai.system-prompt}")
    private String systemPrompt;

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
                ChatMessage systemMessage =
                        new ChatMessage(ChatMessageRole.SYSTEM.value(), chatBotConfig.getSystemPrompt());
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
            ChatMessage assistantMessage = new ChatMessage(ChatMessageRole.ASSISTANT.value(), assistantResponse);
            chatBotConversationService.addMessage(sessionId, assistantMessage);

            return assistantResponse;
        } catch (Exception e) {
            //            log.error("Error generating OpenAI response", e);
            return "I apologize, but I encountered an error processing your request.";
        }
    }

    @Override
    public void processAndRespond(ChatMessageEntity userMessage, Long roomId) {
        // Get or initialize conversation history
        List<ChatMessageEntity> history = conversationHistory.computeIfAbsent(roomId, k -> new ArrayList<>());

        //        List<ChatMessageResponseDTO> history =
        // Optional.ofNullable(chatRoomService.getChatMessageHistory(roomId))
        //                .orElseGet(ArrayList::new);

        // Fetch the chat room and initialize the participants collection
        ChatRoom chatRoom = chatRoomRepository
                .findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("chatroom", "id", roomId));
        userMessage.setChatRoom(chatRoom);
        //        Hibernate.initialize(chatRoom.getParticipants());

        // Add user message to history
        history.add(userMessage);

        // Convert entity messages to OpenAI messages
        List<ChatMessage> openAiMessages = new ArrayList<>();

        // Add system prompt
        openAiMessages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(), systemPrompt));

        // Add conversation history (last 5 messages)
        int historyStart = Math.max(0, history.size() - 5);
        for (ChatMessageEntity msg : history.subList(historyStart, history.size())) {
            String role =
                    msg.getSender().equals(BOT_NAME) ? ChatMessageRole.ASSISTANT.value() : ChatMessageRole.USER.value();
            openAiMessages.add(new ChatMessage(role, msg.getContent()));
        }

        try {
            // Create OpenAI request
            ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                    .messages(openAiMessages)
                    .model("gpt-4o-mini")
                    .maxTokens(300)
                    .temperature(0.7)
                    .build();

            // Get response from OpenAI
            ChatCompletionResult response = openAiService.createChatCompletion(completionRequest);
            String botResponse = response.getChoices().get(0).getMessage().getContent();

            // Create bot message entity
            ChatMessageEntity botMessage = ChatMessageEntity.builder()
                    .sender(BOT_NAME)
                    .content(botResponse)
                    .type(MessageType.CHAT)
                    //                    .chatRoom(chatRoom)
                    .build();

            // Save message to database
            chatRoomService.addMessageToChatRoom(botMessage, roomId);

            // Add to conversation history
            history.add(botMessage);

            ChatMessageEntityRequestDTO botMessageRequestDTO = new ChatMessageEntityRequestDTO(
                    botMessage.getType(), botMessage.getSender(), botMessage.getContent());

            // Send message through WebSocket
            messagingTemplate.convertAndSend("/topic/room/" + roomId, botMessageRequestDTO);

        } catch (Exception e) {
            logger.error("Error generating bot response", e);
            sendErrorMessage(roomId, "I apologize, but I encountered an error processing your request." + e);
        }
    }

    private void sendErrorMessage(Long roomId, String errorMessage) {
        ChatMessageEntity errorBot = ChatMessageEntity.builder()
                .sender(BOT_NAME)
                .content(errorMessage)
                .type(MessageType.CHAT)
                .build();
        messagingTemplate.convertAndSend("/topic/room/" + roomId, errorBot);
    }
}
