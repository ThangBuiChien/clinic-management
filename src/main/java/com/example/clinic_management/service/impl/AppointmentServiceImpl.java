package com.example.clinic_management.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.clinic_management.dtos.requests.AppointmentRequestDTO;
import com.example.clinic_management.dtos.responses.AppointmentResponseDTO;
import com.example.clinic_management.entities.Appointment;
import com.example.clinic_management.entities.Doctor;
import com.example.clinic_management.exception.EmailAlreadyExistedException;
import com.example.clinic_management.exception.ResourceNotFoundException;
import com.example.clinic_management.mapper.AutoAppointmentMapper;
import com.example.clinic_management.repository.AppointmentRepository;
import com.example.clinic_management.repository.DoctorTimeSlotCapacityRepository;
import com.example.clinic_management.service.AppointmentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    private final AutoAppointmentMapper autoAppointmentMapper;

    private final BookingProcessorImpl bookingProcessorImpl;

    private final DoctorTimeSlotCapacityRepository doctorTimeSlotCapacityRepository;

    @Override
    public AppointmentResponseDTO addAppointmentBySelectDoctor(AppointmentRequestDTO appointmentRequestDTO) {
        if (appointmentRequestDTO.getDoctorId() != null
                && !bookingProcessorImpl.checkAvailability(
                        appointmentRequestDTO.getDoctorId(),
                        appointmentRequestDTO.getAppointmentDate(),
                        appointmentRequestDTO.getTimeSlot())) {
            throw new EmailAlreadyExistedException("Doctor is not available for this date and time");
        } else {
            List<Doctor> doctors = bookingProcessorImpl.findAvailableDoctors(
                    appointmentRequestDTO.getDepartmentId(),
                    appointmentRequestDTO.getAppointmentDate(),
                    appointmentRequestDTO.getTimeSlot());
            appointmentRequestDTO.setDoctorId(doctors.get(0).getId());
            bookingProcessorImpl.checkAvailability(
                    appointmentRequestDTO.getDoctorId(),
                    appointmentRequestDTO.getAppointmentDate(),
                    appointmentRequestDTO.getTimeSlot());
        }

        Appointment appointment = autoAppointmentMapper.toEntity(appointmentRequestDTO);
        appointmentRepository.save(appointment);
        return autoAppointmentMapper.toResponseDTO(appointment);
    }

    @Override
    public AppointmentResponseDTO addAppointmentBySelectDepartment(AppointmentRequestDTO appointmentRequestDTO) {
        return null;
    }

    @Override
    public Page<AppointmentResponseDTO> getAllAppointments(Pageable pageable) {
        Page<Appointment> appointments = appointmentRepository.findAll(pageable);
        return appointments.map(autoAppointmentMapper::toResponseDTO);
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
