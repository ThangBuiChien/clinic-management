package com.example.clinic_management.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.clinic_management.dtos.responses.ChatMessageResponseDTO;
import com.example.clinic_management.entities.ChatMessage;
import com.example.clinic_management.entities.ChatRoom;
import com.example.clinic_management.entities.Patient;
import com.example.clinic_management.exception.ResourceNotFoundException;
import com.example.clinic_management.mapper.AutoChatMessageMapper;
import com.example.clinic_management.repository.ChatRoomRepository;
import com.example.clinic_management.repository.PatientRepository;
import com.example.clinic_management.service.ChatRoomService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final PatientRepository patientRepository;
    private final AutoChatMessageMapper autoChatMessageMapper;

    private final Logger logger = LoggerFactory.getLogger(ChatRoomService.class);

    @Override
    public ChatRoom createChatRoom(String roomName, List<Long> userIds) {
        List<Patient> users = patientRepository.findAllById(userIds);
        if (users.size() != userIds.size()) {
            logger.warn("Not all user IDs were found");
        }
        if (users.isEmpty()) {
            throw new RuntimeException("All patient id is not valid");
        }

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRoomName(roomName);
        chatRoom.addParticipants(users);

        return chatRoomRepository.save(chatRoom);
    }

    @Override
    public List<ChatMessageResponseDTO> getChatMessageHistory(Long chatRoomId) {
        return findByIdToEntity(chatRoomId).getMessages().stream()
                .map(autoChatMessageMapper::toResponse)
                .toList();
    }

    @Override
    public List<Long> getAllUserIdInChatRoom(Long chatRoomId) {
        return findByIdToEntity(chatRoomId).getParticipants().stream()
                .map(Patient::getId)
                .toList();
    }

    @Transactional
    @Override
    public void addMessageToChatRoom(ChatMessage chatMessage, Long chatRoomId) {
        ChatRoom chatRoom = findByIdToEntity(chatRoomId);
        chatRoom.addMessage(chatMessage);

        chatRoomRepository.save(chatRoom);
    }

    private ChatRoom findByIdToEntity(Long chatRoomId) {
        return chatRoomRepository
                .findById(chatRoomId)
                .orElseThrow(() -> new ResourceNotFoundException("chatroom", "id", chatRoomId));
    }
}
