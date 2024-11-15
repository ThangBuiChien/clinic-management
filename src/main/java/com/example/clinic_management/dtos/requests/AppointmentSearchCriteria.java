package com.example.clinic_management.dtos.requests;

import com.example.clinic_management.enums.AppointmentStatus;
import com.example.clinic_management.enums.TimeSlot;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

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
