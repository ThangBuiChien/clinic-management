package com.example.clinic_management.controllers.chat;

import java.util.concurrent.CompletableFuture;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.example.clinic_management.dtos.requests.ChatMessageEntityRequestDTO;
import com.example.clinic_management.entities.ChatMessageEntity;
import com.example.clinic_management.entities.ChatRoom;
import com.example.clinic_management.exception.ResourceNotFoundException;
import com.example.clinic_management.repository.ChatRoomRepository;
import com.example.clinic_management.service.chat.ChatBotLocalService;
import com.example.clinic_management.service.chat.ChatBotService;
import com.example.clinic_management.service.chat.ChatRoomService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatRoomRepository chatRoomRepository;

    private final ChatRoomService chatRoomService;

    private final ChatBotService chatBotService;

    private final ChatBotLocalService chatBotLocalService;

    @MessageMapping("/chat.register/{roomId}")
    @SendTo("/topic/room/{roomId}") // Use room ID dynamically
    public ChatMessageEntity register(
            @DestinationVariable Long roomId,
            @Payload ChatMessageEntity chatMessageEntity,
            SimpMessageHeaderAccessor headerAccessor) {
        // Add the username in WebSocket session
        headerAccessor.getSessionAttributes().put("username", chatMessageEntity.getSender());

        // create and store message to db
        ChatMessageEntity newMessage = new ChatMessageEntity();
        newMessage.setSender(chatMessageEntity.getSender());
        newMessage.setType(chatMessageEntity.getType());
        newMessage.setContent(chatMessageEntity.getContent());

        chatRoomService.addMessageToChatRoom(newMessage, roomId);

        return chatMessageEntity; // Notify other users in the room about the new user
    }

    @MessageMapping("/chat.send/{roomId}")
    @SendTo("/topic/room/{roomId}") // Send message to a specific room
    public ChatMessageEntityRequestDTO sendMessage(
            @DestinationVariable Long roomId, @Payload ChatMessageEntityRequestDTO chatMessageEntity) {
        ChatRoom chatRoom = chatRoomRepository
                .findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("chatroom", "id", roomId));

        // create and store message to db
        ChatMessageEntity newMessage = new ChatMessageEntity();
        newMessage.setSender(chatMessageEntity.getSender());
        newMessage.setType(chatMessageEntity.getType());
        newMessage.setChatRoom(chatRoom);
        newMessage.setContent(chatMessageEntity.getContent());

        chatRoomService.addMessageToChatRoom(newMessage, roomId);

        // Process message with chatbot asynchronously if it contains bot trigger
        if (shouldTriggerBot(newMessage.getContent())) {
            //            CompletableFuture.runAsync(() -> chatBotService.processAndRespond(newMessage, roomId));

            CompletableFuture.runAsync(() -> chatBotLocalService.processAndRespond(newMessage, roomId));
        }

        return chatMessageEntity; // Broadcast the message to the room
    }

    private boolean shouldTriggerBot(String message) {
        String lowercaseMessage = message.toLowerCase();
        return lowercaseMessage.startsWith("@bot")
                || lowercaseMessage.startsWith("bot:")
                || lowercaseMessage.contains("@clinicbot");
    }
}
