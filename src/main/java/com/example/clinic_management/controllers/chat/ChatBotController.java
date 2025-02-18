package com.example.clinic_management.controllers.chat;

import java.util.Optional;
import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.clinic_management.dtos.requests.ChatBotRequestDTO;
import com.example.clinic_management.dtos.responses.ApiResponse;
import com.example.clinic_management.dtos.responses.ChatBotResponseDTO;
import com.example.clinic_management.service.chat.ChatBotLocalService;
import com.example.clinic_management.service.chat.ChatBotService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/chat_bot")
public class ChatBotController {

    private final ChatBotService chatBotService;

    private final ChatBotLocalService chatBotLocalService;

    @PostMapping
    public ResponseEntity<ApiResponse> chat(@RequestBody @Valid ChatBotRequestDTO request) {
        //        String response = chatBotService.generateResponse(request.getMessage());

        String sessionId = Optional.ofNullable(request.getSessionId())
                .orElse(UUID.randomUUID().toString());

        String response = chatBotService.generateResponse(sessionId, request.getMessage());

        return ResponseEntity.ok(ApiResponse.builder()
                .message("ChatBot said...")
                .result(new ChatBotResponseDTO(response, sessionId))
                .build());
    }

    @PostMapping("/local_bot")
    public ResponseEntity<ApiResponse> chatLocal(@RequestBody @Valid ChatBotRequestDTO request) {
        String response = chatBotLocalService.getChatBotResponse(request.getMessage());

        return ResponseEntity.ok(ApiResponse.builder()
                .message("ChatBot said...")
                .result(new ChatBotResponseDTO(response, null))
                .build());
    }
}
