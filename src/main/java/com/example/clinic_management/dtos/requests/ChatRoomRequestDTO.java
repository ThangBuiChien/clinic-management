package com.example.clinic_management.dtos.requests;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChatRoomRequestDTO {
    private String roomName;
    private List<Long> userIds;
}
