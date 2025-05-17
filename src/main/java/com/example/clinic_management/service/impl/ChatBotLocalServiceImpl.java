package com.example.clinic_management.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.example.clinic_management.security.user.EShopUserDetail;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.clinic_management.dtos.requests.ChatMessageEntityRequestDTO;
import com.example.clinic_management.entities.ChatMessageEntity;
import com.example.clinic_management.entities.ChatRoom;
import com.example.clinic_management.enums.MessageType;
import com.example.clinic_management.exception.ResourceNotFoundException;
import com.example.clinic_management.repository.ChatRoomRepository;
import com.example.clinic_management.service.chat.ChatBotLocalService;
import com.example.clinic_management.service.chat.ChatRoomService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatBotLocalServiceImpl implements ChatBotLocalService {

    @Value("${chatbot.api.url}")
    private String chatBotApiUrl;

    private final RestTemplate restTemplate;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomService chatRoomService;
    private final SimpMessagingTemplate messagingTemplate;

    private final Map<Long, List<ChatMessageEntity>> conversationHistory = new ConcurrentHashMap<>();

    @Override
    public String getChatBotResponse(String question) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 👇 Add userId as custom header
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.getPrincipal() instanceof EShopUserDetail userDetails) {
                Long currentId =  userDetails.getId();
                headers.add("X-User-Id", currentId.toString());
            }

            JSONObject request = new JSONObject();
            request.put("question", question);

            HttpEntity<String> entity = new HttpEntity<>(request.toString(), headers);

            ResponseEntity<String> response =
                    restTemplate.exchange(chatBotApiUrl + "/chat", HttpMethod.POST, entity, String.class);

            return response.getBody();
        } catch (Exception e) {
            return "Sorry, I am not able to respond to that question at the moment. The server is down.";
        }
    }

    @Override
    public void processAndRespond(ChatMessageEntity userMessage, Long roomId) {

        List<ChatMessageEntity> history = conversationHistory.computeIfAbsent(roomId, k -> new ArrayList<>());

        ChatRoom chatRoom = chatRoomRepository
                .findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("chatroom", "id", roomId));
        userMessage.setChatRoom(chatRoom);

        history.add(userMessage);

        String response = getChatBotResponse(userMessage.getContent());

        // Create bot message entity
        ChatMessageEntity botMessage = ChatMessageEntity.builder()
                .sender("SUPERR_CLINIC_BOT")
                .content(response)
                .type(MessageType.CHAT)
                .build();

        // Save message to database
        chatRoomService.addMessageToChatRoom(botMessage, roomId);

        // Add to conversation history
        history.add(botMessage);

        ChatMessageEntityRequestDTO botMessageRequestDTO =
                new ChatMessageEntityRequestDTO(botMessage.getType(), botMessage.getSender(), botMessage.getContent());

        // Send message through WebSocket
        messagingTemplate.convertAndSend("/topic/room/" + roomId, botMessageRequestDTO);
    }
}
