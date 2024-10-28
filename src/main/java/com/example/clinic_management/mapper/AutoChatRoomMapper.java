package com.example.clinic_management.mapper;

import com.example.clinic_management.dtos.responses.ChatRoomResponseDTO;
import com.example.clinic_management.entities.ChatRoom;
import com.example.clinic_management.entities.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {AutoChatMessageMapper.class})

public interface AutoChatRoomMapper {

    @Mapping(target = "participantsId", source = "participants")
    ChatRoomResponseDTO toResponseDTO(ChatRoom chatRoom);

    default Set<Long> mapParticipantsToIds(Set<Patient> participants) {
        return participants.stream()
                .map(Patient::getId)
                .collect(Collectors.toSet());
    }
}
