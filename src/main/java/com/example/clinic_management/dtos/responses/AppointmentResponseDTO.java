package com.example.clinic_management.dtos.responses;

import java.time.LocalDate;

import com.example.clinic_management.enums.AppointmentStatus;
import com.example.clinic_management.enums.TimeSlot;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentResponseDTO {

    private Long id;
    private LocalDate appointmentDate;
    //    private Doctor doctor;
    //    private Patient patient;
    private String doctorName;
    private Long doctorId;
    private String patientName;
    private Long patientId;
    private AppointmentStatus appointmentStatus;
    private TimeSlot timeSlot;

    private Long payId;
}
