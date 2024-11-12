package com.example.clinic_management.dtos.responses;

import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomResponseDTO {

    private Long id;

    private String roomName;

    private Set<Long> participantsId;

    private List<ChatMessageResponseDTO> messages;
}
