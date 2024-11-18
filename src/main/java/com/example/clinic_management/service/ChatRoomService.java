package com.example.clinic_management.service;

import java.util.List;

import com.example.clinic_management.dtos.responses.ChatMessageResponseDTO;
import com.example.clinic_management.dtos.responses.ChatRoomResponseDTO;
import com.example.clinic_management.entities.ChatMessageEntity;

public interface ChatRoomService {
    public ChatRoomResponseDTO createChatRoom(String roomName, List<Long> userIds);

    public List<ChatMessageResponseDTO> getChatMessageHistory(Long chatRoomId);

    public List<Long> getAllUserIdInChatRoom(Long chatRoomId);

    public void addMessageToChatRoom(ChatMessageEntity chatMessageEntity, Long chatRoomId);
}
