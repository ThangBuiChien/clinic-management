package com.example.clinic_management.dtos.requests;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

import com.example.clinic_management.enums.TimeSlot;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAppointmentDateAndTime {

    @NotNull
    private LocalDate appointmentDate;

    @NotNull
    private TimeSlot timeSlot;
}
