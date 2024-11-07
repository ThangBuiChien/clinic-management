package com.example.clinic_management.dtos.responses;

import com.example.clinic_management.entities.ChatMessage;
import com.example.clinic_management.entities.Patient;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class ChatRoomResponseDTO {

    private Long id;

    private String roomName;

    private Set<Long> participantsId;

    private List<ChatMessageResponseDTO> messages;
}
