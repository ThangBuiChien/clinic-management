package com.example.clinic_management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.clinic_management.dtos.responses.ChatMessageResponseDTO;
import com.example.clinic_management.entities.ChatMessage;

@Mapper(componentModel = "spring")
public interface AutoChatMessageMapper {

    @Mapping(target = "chatRoomId", source = "chatRoom.id")
    ChatMessageResponseDTO toResponse(ChatMessage chatMessage);
}
