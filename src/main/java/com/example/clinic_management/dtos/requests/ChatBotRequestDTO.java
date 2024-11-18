package com.example.clinic_management.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatBotRequestDTO {
    private String message;
    private String sessionId; // Added to track conversations
}
