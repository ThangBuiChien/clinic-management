package com.example.clinic_management.controllers;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.example.clinic_management.entities.ChatMessage;
import com.example.clinic_management.entities.ChatRoom;
import com.example.clinic_management.exception.ResourceNotFoundException;
import com.example.clinic_management.repository.ChatRoomRepository;
import com.example.clinic_management.service.ChatRoomService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatRoomRepository chatRoomRepository;

    private final ChatRoomService chatRoomService;

    @MessageMapping("/chat.register/{roomId}")
    @SendTo("/topic/room/{roomId}") // Use room ID dynamically
    public ChatMessage register(
            @DestinationVariable Long roomId,
            @Payload ChatMessage chatMessage,
            SimpMessageHeaderAccessor headerAccessor) {
        // Add the username in WebSocket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());

        // create and store message to db
        ChatMessage newMessage = new ChatMessage();
        newMessage.setSender(chatMessage.getSender());
        newMessage.setType(chatMessage.getType());
        newMessage.setContent(chatMessage.getContent());

        chatRoomService.addMessageToChatRoom(newMessage, roomId);

        return chatMessage; // Notify other users in the room about the new user
    }

    @MessageMapping("/chat.send/{roomId}")
    @SendTo("/topic/room/{roomId}") // Send message to a specific room
    public ChatMessage sendMessage(@DestinationVariable Long roomId, @Payload ChatMessage chatMessage) {
        ChatRoom chatRoom = chatRoomRepository
                .findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("chatroom", "id", roomId));

        // create and store message to db
        ChatMessage newMessage = new ChatMessage();
        newMessage.setSender(chatMessage.getSender());
        newMessage.setType(chatMessage.getType());
        newMessage.setChatRoom(chatRoom);
        newMessage.setContent(chatMessage.getContent());

        chatRoomService.addMessageToChatRoom(newMessage, roomId);

        return chatMessage; // Broadcast the message to the room
    }
}
