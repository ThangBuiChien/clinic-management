package com.example.clinic_management.controllers;

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
import com.example.clinic_management.service.ChatBotService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/chat_bot")
public class ChatBotController {

    private final ChatBotService chatBotService;

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
}
