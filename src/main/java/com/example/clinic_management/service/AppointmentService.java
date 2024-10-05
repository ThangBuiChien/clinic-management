package com.example.clinic_management.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.clinic_management.dtos.requests.AppointmentRequestDTO;
import com.example.clinic_management.dtos.responses.AppointmentResponseDTO;
import com.example.clinic_management.enums.AppointmentStatus;

public interface AppointmentService {

    AppointmentResponseDTO addAppointment(AppointmentRequestDTO appointmentRequestDTO);

    Page<AppointmentResponseDTO> getAllAppointments(Pageable pageable);

    AppointmentResponseDTO getAppointmentById(Long id);

    AppointmentResponseDTO getAppointmentByDoctorIdAndDate(Long doctorId, LocalDate date);

    AppointmentResponseDTO updateAppointmentStatus(Long id, AppointmentStatus appointmentStatus);
}
