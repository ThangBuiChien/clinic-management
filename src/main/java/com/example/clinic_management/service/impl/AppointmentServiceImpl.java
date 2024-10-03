package com.example.clinic_management.service.impl;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.example.clinic_management.dtos.requests.AppointmentRequestDTO;
import com.example.clinic_management.dtos.responses.AppointmentResponseDTO;
import com.example.clinic_management.entities.Appointment;
import com.example.clinic_management.exception.ResourceNotFoundException;
import com.example.clinic_management.mapper.AutoAppointmentMapper;
import com.example.clinic_management.repository.AppointmentRepository;
import com.example.clinic_management.service.AppointmentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    private final AutoAppointmentMapper autoAppointmentMapper;

    @Override
    public AppointmentResponseDTO addAppointment(AppointmentRequestDTO appointmentRequestDTO) {
        Appointment appointment = autoAppointmentMapper.toEntity(appointmentRequestDTO);
        appointmentRepository.save(appointment);
        return autoAppointmentMapper.toResponseDTO(appointment);
    }

    @Override
    public AppointmentResponseDTO getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", id));

        return autoAppointmentMapper.toResponseDTO(appointment);
    }

    @Override
    public AppointmentResponseDTO getAppointmentByDoctorIdAndDate(Long doctorId, LocalDate date) {
        Appointment appointment = appointmentRepository.findByDoctorIdAndAppointmentDate(doctorId, date);
        return autoAppointmentMapper.toResponseDTO(appointment);
    }
}
