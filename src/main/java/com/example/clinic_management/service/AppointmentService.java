package com.example.clinic_management.service;

import java.time.LocalDate;

import com.example.clinic_management.dtos.requests.AppointmentRequestDTO;
import com.example.clinic_management.dtos.responses.AppointmentResponseDTO;

public interface AppointmentService {

    AppointmentResponseDTO addAppointment(AppointmentRequestDTO appointmentRequestDTO);

    AppointmentResponseDTO getAppointmentById(Long id);

    AppointmentResponseDTO getAppointmentByDoctorIdAndDate(Long doctorId, LocalDate date);
}
