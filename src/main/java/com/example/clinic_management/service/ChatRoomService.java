package com.example.clinic_management.service;

import com.example.clinic_management.entities.ChatMessage;
import com.example.clinic_management.entities.ChatRoom;

import java.util.List;

public interface ChatRoomService {
    public ChatRoom createChatRoom(String roomName, List<Long> userIds);
    public List<ChatMessage> getChatMessageHistory(Long chatRoomId);
    public List<Long> getAllUserIdInChatRoom(Long chatRoomId);

    public void addMessageToChatRoom(ChatMessage chatMessage, Long chatRoomId);




}
