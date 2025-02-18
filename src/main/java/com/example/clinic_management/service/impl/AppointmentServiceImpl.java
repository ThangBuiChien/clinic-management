package com.example.clinic_management.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.clinic_management.dtos.requests.*;
import com.example.clinic_management.dtos.responses.AppointmentResponseDTO;
import com.example.clinic_management.entities.Appointment;
import com.example.clinic_management.entities.Doctor;
import com.example.clinic_management.entities.DoctorTimeslotCapacity;
import com.example.clinic_management.enums.AppointmentStatus;
import com.example.clinic_management.exception.ResourceNotFoundException;
import com.example.clinic_management.mapper.AutoAppointmentMapper;
import com.example.clinic_management.repository.AppointmentRepository;
import com.example.clinic_management.repository.DoctorTimeSlotCapacityRepository;
import com.example.clinic_management.repository.ScheduleRepository;
import com.example.clinic_management.service.booking.AppointmentService;
import com.example.clinic_management.specification.AppointmentSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    private final AutoAppointmentMapper autoAppointmentMapper;

    private final BookingProcessorImpl bookingProcessorImpl;

    private final DoctorTimeSlotCapacityRepository doctorTimeSlotCapacityRepository;

    private final ScheduleRepository scheduleRepository;

    private final AppointmentSpecification appointmentSpecification;

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
    public AppointmentResponseDTO addAppointmentBySelectDoctor(
            AddAppointmentRequestByDoctorDTO addAppointmentRequestByDoctorDTO) {

        DoctorTimeslotCapacity doctorTimeslotCapacity =
                bookingProcessorImpl.getOrCreateDoctorTimeSlotCapacityIfInWorkingDay(
                        addAppointmentRequestByDoctorDTO.getDoctorId(),
                        addAppointmentRequestByDoctorDTO.getAppointmentDate(),
                        addAppointmentRequestByDoctorDTO.getTimeSlot());
        if (!doctorTimeslotCapacity.canAcceptMorePatients()) {
            throw new RuntimeException("Doctors is busy in this date and time");
        }

        // Increase slot capacity in doctor_timeslot_capacity
        doctorTimeslotCapacity.addPatient();

        Appointment appointment = autoAppointmentMapper.toEntity(addAppointmentRequestByDoctorDTO);
        appointment.setAppointmentStatus(AppointmentStatus.PENDING);
        appointmentRepository.save(appointment);
        return autoAppointmentMapper.toResponseDTO(appointment);
    }

    @Override
    public AppointmentResponseDTO addAppointmentBySelectDepartment(
            AddAppointmentRequestByDepartmentDTO addAppointmentRequestByDepartmentDTO) {
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
        List<Appointment> appointment = appointmentRepository.findByDoctorIdAndAppointmentDate(
                doctorId, date, Sort.by(Sort.Direction.ASC, "timeSlot"));
        return appointment.stream().map(autoAppointmentMapper::toResponseDTO).toList();
    }

    @Override
    public Page<AppointmentResponseDTO> getAppointmentByPatientId(Long patientId, Pageable pageable) {
        return appointmentRepository.findByPatientId(patientId, pageable).map(autoAppointmentMapper::toResponseDTO);
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

    @Transactional
    @Override
    public AppointmentResponseDTO updateAppointmentSchedule(
            Long id, UpdateAppointmentDateAndTime updateAppointmentDateAndTime) {

        // find by id -> check if is validated new schedule or not -> check timeslot new day
        // -> remove capacity old and add to new day, set new date, time slot to appointment and save
        Appointment appointment = appointmentRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("appointment", "id", id));

        isNewDateFarEnough(appointment.getAppointmentDate());

        checkSameDateAndTime(appointment, updateAppointmentDateAndTime);

        DoctorTimeslotCapacity newDoctorTimeslotCapacity =
                bookingProcessorImpl.getOrCreateDoctorTimeSlotCapacityIfInWorkingDay(
                        appointment.getDoctor().getId(),
                        updateAppointmentDateAndTime.getAppointmentDate(),
                        updateAppointmentDateAndTime.getTimeSlot());
        if (!newDoctorTimeslotCapacity.canAcceptMorePatients()) {
            throw new RuntimeException("Doctors is busy in this date and time");
        }

        // Increase slot capacity in doctor_timeslot_capacity
        newDoctorTimeslotCapacity.addPatient();
        doctorTimeSlotCapacityRepository.save(newDoctorTimeslotCapacity);

        // Remove slot capacity in old doctor_timeslot_capacity
        DoctorTimeslotCapacity oldDoctorTime = bookingProcessorImpl.getOrCreateDoctorTimeSlotCapacityIfInWorkingDay(
                appointment.getDoctor().getId(), appointment.getAppointmentDate(), appointment.getTimeSlot());
        oldDoctorTime.removePatient();
        doctorTimeSlotCapacityRepository.save(oldDoctorTime);

        appointment.setAppointmentDate(updateAppointmentDateAndTime.getAppointmentDate());
        appointment.setTimeSlot(updateAppointmentDateAndTime.getTimeSlot());

        appointmentRepository.save(appointment);

        return autoAppointmentMapper.toResponseDTO(appointment);
    }

    @Override
    public Page<AppointmentResponseDTO> searchAppointment(
            AppointmentSearchCriteria appointmentSearchCriteria, Pageable pageable) {
        Specification<Appointment> spec = appointmentSpecification.getSearchSpecification(appointmentSearchCriteria);
        return appointmentRepository.findAll(spec, pageable).map(autoAppointmentMapper::toResponseDTO);
    }

    private void isNewDateFarEnough(LocalDate oldAppointmentDate) {
        LocalDate currentDate = LocalDate.now();

        if (oldAppointmentDate.isBefore(currentDate.plusDays(2))) {
            throw new RuntimeException("Can not reschedule before 2 days of currently booking date");
        }
    }

    private void checkSameDateAndTime(
            Appointment oldAppointment, UpdateAppointmentDateAndTime updateAppointmentDateAndTime) {
        if (oldAppointment.getAppointmentDate().isEqual(updateAppointmentDateAndTime.getAppointmentDate())
                && oldAppointment.getTimeSlot().equals(updateAppointmentDateAndTime.getTimeSlot())) {
            throw new RuntimeException("Can not reschedule in the same date and timeslot");
        }
    }
}
