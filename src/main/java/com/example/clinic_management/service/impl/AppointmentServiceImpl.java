package com.example.clinic_management.service.impl;

import java.time.LocalDate;
import java.util.List;

import com.example.clinic_management.dtos.requests.AddAppointmentRequestByDepartmentDTO;
import com.example.clinic_management.dtos.requests.AddAppointmentRequestByDoctorDTO;
import com.example.clinic_management.entities.DoctorTimeslotCapacity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.clinic_management.dtos.requests.AppointmentRequestDTO;
import com.example.clinic_management.dtos.responses.AppointmentResponseDTO;
import com.example.clinic_management.entities.Appointment;
import com.example.clinic_management.entities.Doctor;
import com.example.clinic_management.enums.AppointmentStatus;
import com.example.clinic_management.exception.ResourceNotFoundException;
import com.example.clinic_management.mapper.AutoAppointmentMapper;
import com.example.clinic_management.repository.AppointmentRepository;
import com.example.clinic_management.repository.DoctorTimeSlotCapacityRepository;
import com.example.clinic_management.repository.ScheduleRepository;
import com.example.clinic_management.service.AppointmentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    private final AutoAppointmentMapper autoAppointmentMapper;

    private final BookingProcessorImpl bookingProcessorImpl;

    private final DoctorTimeSlotCapacityRepository doctorTimeSlotCapacityRepository;

    private final ScheduleRepository scheduleRepository;

    @Override
    public AppointmentResponseDTO addAppointment(AppointmentRequestDTO appointmentRequestDTO) {
        if (appointmentRequestDTO.getDoctorId() != null
                && !bookingProcessorImpl.checkAvailability(
                        appointmentRequestDTO.getDoctorId(),
                        appointmentRequestDTO.getAppointmentDate(),
                        appointmentRequestDTO.getTimeSlot())) {
            throw new RuntimeException("No available doctors for this date and time");
        } else {
            List<Doctor> doctors = bookingProcessorImpl.findAvailableDoctors(
                    appointmentRequestDTO.getDepartmentId(),
                    appointmentRequestDTO.getAppointmentDate(),
                    appointmentRequestDTO.getTimeSlot());
            if (doctors.isEmpty()) {
                throw new RuntimeException("No available doctors for this date and time");
            }
            appointmentRequestDTO.setDoctorId(doctors.get(0).getId());
            bookingProcessorImpl.checkAvailability(
                    appointmentRequestDTO.getDoctorId(),
                    appointmentRequestDTO.getAppointmentDate(),
                    appointmentRequestDTO.getTimeSlot());
        }

        doctorTimeSlotCapacityRepository
                .findByScheduleIdAndTimeSlot(
                        scheduleRepository
                                .findByDoctorIdAndDate(
                                        appointmentRequestDTO.getDoctorId(), appointmentRequestDTO.getAppointmentDate())
                                .getId(),
                        appointmentRequestDTO.getTimeSlot())
                .addPatient();
        Appointment appointment = autoAppointmentMapper.toEntity(appointmentRequestDTO);
        appointmentRepository.save(appointment);
        return autoAppointmentMapper.toResponseDTO(appointment);
    }

    @Override
    public AppointmentResponseDTO addAppointmentBySelectDoctor(AddAppointmentRequestByDoctorDTO addAppointmentRequestByDoctorDTO) {

        DoctorTimeslotCapacity doctorTimeslotCapacity = bookingProcessorImpl.getOrCreateDoctorTimeSlotCapacityIfInWorkingDay(
                addAppointmentRequestByDoctorDTO.getDoctorId(),
                addAppointmentRequestByDoctorDTO.getAppointmentDate(),
                addAppointmentRequestByDoctorDTO.getTimeSlot());
        if(!doctorTimeslotCapacity.canAcceptMorePatients()){
            throw new RuntimeException("Doctors is busy in this date and time");
        }

        //Increase slot capacity in doctor_timeslot_capacity
        doctorTimeslotCapacity.addPatient();

        Appointment appointment = autoAppointmentMapper.toEntity(addAppointmentRequestByDoctorDTO);
        appointment.setAppointmentStatus(AppointmentStatus.PENDING);
        appointmentRepository.save(appointment);
        return autoAppointmentMapper.toResponseDTO(appointment);

    }

    @Override
    public AppointmentResponseDTO addAppointmentBySelectDepartment(AddAppointmentRequestByDepartmentDTO addAppointmentRequestByDepartmentDTO) {
        List<Doctor> doctors = bookingProcessorImpl.findAvailableDoctors(
                addAppointmentRequestByDepartmentDTO.getDepartmentId(),
                addAppointmentRequestByDepartmentDTO.getAppointmentDate(),
                addAppointmentRequestByDepartmentDTO.getTimeSlot());
        if (doctors.isEmpty()) {
            throw new RuntimeException("No available doctors for this date and time");
        }
//        addAppointmentRequestByDepartmentDTO.setDoctorId(doctors.get(0).getId());
        AddAppointmentRequestByDoctorDTO addAppointmentRequestByDoctorDTO = new AddAppointmentRequestByDoctorDTO();
        addAppointmentRequestByDoctorDTO.setAppointmentDate(addAppointmentRequestByDepartmentDTO.getAppointmentDate());
        addAppointmentRequestByDoctorDTO.setDoctorId(doctors.get(0).getId());
        addAppointmentRequestByDoctorDTO.setPatientId(addAppointmentRequestByDepartmentDTO.getPatientId());
        addAppointmentRequestByDoctorDTO.setTimeSlot(addAppointmentRequestByDepartmentDTO.getTimeSlot());

        return addAppointmentBySelectDoctor(addAppointmentRequestByDoctorDTO);
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
    public List<AppointmentResponseDTO> getAppointmentByDoctorIdAndDate(Long doctorId, LocalDate date) {
        List<Appointment> appointment = appointmentRepository.findByDoctorIdAndAppointmentDate(doctorId, date);
        return appointment.stream().map(autoAppointmentMapper::toResponseDTO).toList();
    }

    @Override
    public AppointmentResponseDTO updateAppointmentStatus(Long id, AppointmentStatus appointmentStatus) {
        return appointmentRepository
                .findById(id)
                .map(appointment -> {
                    appointment.setAppointmentStatus(appointmentStatus);
                    return appointmentRepository.save(appointment);
                })
                .map(autoAppointmentMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", id));
    }
}
