package com.example.clinic_management.dtos.requests;

import java.time.LocalDate;

import com.example.clinic_management.enums.AppointmentStatus;
import com.example.clinic_management.enums.TimeSlot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentSearchCriteria {

    private Long id;
    private LocalDate appointmentDate;
    private Long doctorId;
    private Long patientId;
    private AppointmentStatus appointmentStatus;
    private TimeSlot timeSlot;
}
