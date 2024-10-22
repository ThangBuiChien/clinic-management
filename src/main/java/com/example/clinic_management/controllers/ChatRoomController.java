package com.example.clinic_management.controllers;

import com.example.clinic_management.dtos.requests.ChatRoomRequestDTO;
import com.example.clinic_management.dtos.responses.ApiResponse;
import com.example.clinic_management.entities.ChatMessage;
import com.example.clinic_management.service.ChatRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/chat_room")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @PostMapping("")
    public ResponseEntity<ApiResponse> createChatRoom(@RequestBody @Valid ChatRoomRequestDTO chatRoomRequest){
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Chatroom created successfully")
                .result(chatRoomService.createChatRoom(chatRoomRequest.getRoomName(), chatRoomRequest.getUserIds()))
                .build());
    }

    @GetMapping("/history/{roomId}")
    public List<ChatMessage> getChatHistory(@PathVariable Long roomId) {
        return chatRoomService.getChatMessageHistory(roomId);
    }

    @GetMapping("participants/{roomId}")
    public List<Long> getParticipantsId(@PathVariable Long roomId) {
        return chatRoomService.getAllUserIdInChatRoom(roomId);
    }
}
