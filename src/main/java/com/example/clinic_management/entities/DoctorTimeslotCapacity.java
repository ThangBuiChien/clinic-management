package com.example.clinic_management.entities;

import jakarta.persistence.*;

import com.example.clinic_management.enums.TimeSlot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "doctor_timeslot_capacity")
public class DoctorTimeslotCapacity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private TimeSlot timeSlot;
    private int maxPatients;
    private int currentPatients;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    public boolean canAcceptMorePatients() {
        return currentPatients <= maxPatients;
    }

    public void addPatient() {
        currentPatients++;
    }
}
