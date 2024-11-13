package com.example.clinic_management.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomName;

    @ManyToMany
    @JoinTable(
            name = "chat_room_participants",
            joinColumns = @JoinColumn(name = "chat_room_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<Patient> participants = new HashSet<>();

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessageEntity> messages = new ArrayList<>();

    public void addMessage(ChatMessageEntity message) {
        messages.add(message);
        message.setChatRoom(this);
    }

    public void deleteMessage(ChatMessageEntity message) {
        messages.remove(message);
        message.setChatRoom(null);
    }

    public void addParticipant(Patient patient) {
        participants.add(patient);
        patient.getChatRooms().add(this);
    }

    public void addParticipants(List<Patient> patients) {
        for (Patient patient : patients) {
            addParticipant(patient);
        }
    }

    public void removeParticipants(Patient patient) {
        participants.remove(patient);
        patient.getChatRooms().remove(this);
    }
}
