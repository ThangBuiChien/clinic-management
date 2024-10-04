package com.example.clinic_management.dtos.requests;

import java.time.LocalDate;

import com.example.clinic_management.enums.AppointmentStatus;
import com.example.clinic_management.enums.TimeSlot;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentRequestDTO {
    private Long doctorId;
    private Long patientId;
    private AppointmentStatus appointmentStatus;
    private LocalDate appointmentDate;
    private TimeSlot timeSlot;
}
