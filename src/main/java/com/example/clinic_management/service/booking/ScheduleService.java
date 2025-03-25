package com.example.clinic_management.service.booking;

import java.time.LocalDate;

import com.example.clinic_management.dtos.responses.ScheduleResponseDTO;

public interface ScheduleService {

    ScheduleResponseDTO getSchedulesByDoctorIdAndDate(Long doctorId, LocalDate date);
}
