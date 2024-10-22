package com.example.clinic_management.service;

import java.util.List;

import com.example.clinic_management.dtos.responses.ChatMessageResponseDTO;
import com.example.clinic_management.entities.ChatMessage;
import com.example.clinic_management.entities.ChatRoom;

public interface ChatRoomService {
    public ChatRoom createChatRoom(String roomName, List<Long> userIds);

    public List<ChatMessageResponseDTO> getChatMessageHistory(Long chatRoomId);

    public List<Long> getAllUserIdInChatRoom(Long chatRoomId);

    public void addMessageToChatRoom(ChatMessage chatMessage, Long chatRoomId);
}
