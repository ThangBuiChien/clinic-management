package com.example.clinic_management.dtos.requests;

import com.example.clinic_management.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageEntityRequestDTO {

    private MessageType type;
    private String sender;
    private String content;
}
