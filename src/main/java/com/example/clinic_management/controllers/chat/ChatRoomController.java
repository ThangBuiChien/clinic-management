package com.example.clinic_management.controllers.chat;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.clinic_management.dtos.requests.ChatRoomRequestDTO;
import com.example.clinic_management.dtos.responses.ApiResponse;
import com.example.clinic_management.dtos.responses.ChatMessageResponseDTO;
import com.example.clinic_management.service.chat.ChatRoomService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/chat_room")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @PostMapping("")
    public ResponseEntity<ApiResponse> createChatRoom(@RequestBody @Valid ChatRoomRequestDTO chatRoomRequest) {
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Chatroom created successfully")
                .result(chatRoomService.createChatRoom(chatRoomRequest.getRoomName(), chatRoomRequest.getUserIds()))
                .build());
    }

    @GetMapping("/history/{roomId}")
    public List<ChatMessageResponseDTO> getChatHistory(@PathVariable Long roomId) {
        return chatRoomService.getChatMessageHistory(roomId);
    }

    @GetMapping("participants/{roomId}")
    public List<Long> getParticipantsId(@PathVariable Long roomId) {
        return chatRoomService.getAllUserIdInChatRoom(roomId);
    }
}
