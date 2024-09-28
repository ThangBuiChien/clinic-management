package com.example.clinic_management.dtos.responses;

import java.time.LocalDate;

import com.example.clinic_management.entities.Doctor;
import com.example.clinic_management.entities.Patient;
import com.example.clinic_management.enums.AppointmentStatus;
import com.example.clinic_management.enums.TimeSlot;

public class AppointmentResponseDTO {

    private Long id;
    private LocalDate appointmentDate;
    private Doctor doctor;
    private Patient patient;
    private AppointmentStatus appointmentStatus;
    private TimeSlot timeSlot;
}
