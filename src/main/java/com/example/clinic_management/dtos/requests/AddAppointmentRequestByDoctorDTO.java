package com.example.clinic_management.dtos.requests;

import java.time.LocalDate;

import com.example.clinic_management.enums.TimeSlot;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddAppointmentRequestByDoctorDTO {
    private Long doctorId;
    private Long patientId;
    private LocalDate appointmentDate;
    private TimeSlot timeSlot;
}
