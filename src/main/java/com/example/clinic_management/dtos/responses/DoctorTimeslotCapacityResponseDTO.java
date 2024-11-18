package com.example.clinic_management.dtos.responses;

import com.example.clinic_management.enums.TimeSlot;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorTimeslotCapacityResponseDTO {

    private Long id;

    private TimeSlot timeSlot;
    private int maxPatients;
    private int currentPatients;
}
