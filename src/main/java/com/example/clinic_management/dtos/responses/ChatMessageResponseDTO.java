package com.example.clinic_management.dtos.responses;

import com.example.clinic_management.enums.MessageType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageResponseDTO {

    private Long id;
    private MessageType type;
    private String sender;
    private String content;
    private Long chatRoomId;
}
