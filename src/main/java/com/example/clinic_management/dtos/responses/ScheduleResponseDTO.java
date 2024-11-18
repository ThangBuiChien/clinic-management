package com.example.clinic_management.dtos.responses;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleResponseDTO {

    private Long id;

    private LocalDate date;

    private Long doctorId;

    private Set<DoctorTimeslotCapacityResponseDTO> doctorTimeslotCapacityResponseDTOS;
}
