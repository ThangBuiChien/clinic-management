package com.example.clinic_management.dtos.requests;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomRequestDTO {
    private String roomName;
    private List<Long> userIds;
}
