package com.example.clinic_management.service.impl;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.example.clinic_management.entities.Doctor;
import com.example.clinic_management.entities.DoctorTimeslotCapacity;
import com.example.clinic_management.entities.Schedule;
import com.example.clinic_management.enums.TimeSlot;
import com.example.clinic_management.exception.ResourceNotFoundException;
import com.example.clinic_management.repository.DepartmentRepository;
import com.example.clinic_management.repository.DoctorRepository;
import com.example.clinic_management.repository.DoctorTimeSlotCapacityRepository;
import com.example.clinic_management.repository.ScheduleRepository;
import com.example.clinic_management.service.BookingProcessor;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingProcessorImpl implements BookingProcessor {

    private final DoctorRepository doctorRepository;

    private final ScheduleRepository scheduleRepository;

    private final DoctorTimeSlotCapacityRepository doctorTimeSlotCapacityRepository;

    private final DepartmentRepository departmentRepository;

    @Override
    public boolean checkAvailability(Long doctorId, LocalDate date, TimeSlot timeSlot) {
        Doctor doctor = doctorRepository
                .findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", doctorId));

        Schedule schedule = scheduleRepository.findByDoctorIdAndDate(doctorId, date);

        if (schedule == null && !doctor.isWorkingDay(date.getDayOfWeek())) {
            return false;
        }

        // Create schedule if doctor in working day and not exists
        if (schedule == null) {
            schedule = createScheduleWithFullTimeSlot(doctor, date);
        }

        // Increase current patients for the given timeslot
        doctorTimeSlotCapacityRepository
                .findByScheduleIdAndTimeSlot(schedule.getId(), timeSlot)
                .addPatient();

        return doctorTimeSlotCapacityRepository
                .findByScheduleIdAndTimeSlot(schedule.getId(), timeSlot)
                .canAcceptMorePatients();
    }

    @Override
    public List<Doctor> findAvailableDoctors(Long departmentId, LocalDate date, TimeSlot timeSlot) {
        departmentRepository
                .findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", departmentId));

        List<Doctor> departmentDoctor = doctorRepository.findAllByDepartmentIdAndWorkingDaysIn(
                departmentId, Collections.singleton(date.getDayOfWeek()));

        if (departmentDoctor.isEmpty()) {
            throw new ResourceNotFoundException("Doctor", "departmentId", departmentId);
        }

        // Filter all doctors which are in their working days
        // Then check if the doctor has a schedule for the given date
        // If not, create a schedule with full timeslots
        // Then check if the doctor can accept more patients for the given timeslot
        List<Doctor> availableDoctors = departmentDoctor.stream()
                .filter(doctor -> doctor.isWorkingDay(date.getDayOfWeek()))
                .map(doctor -> {
                    Schedule schedule = scheduleRepository.findByDoctorIdAndDate(doctor.getId(), date);

                    if (schedule == null) {
                        schedule = createScheduleWithFullTimeSlot(doctor, date);
                    }

                    return doctorTimeSlotCapacityRepository
                                    .findByScheduleIdAndTimeSlot(schedule.getId(), timeSlot)
                                    .canAcceptMorePatients()
                            ? doctor
                            : null;
                })
                .filter(Objects::nonNull)
                .toList();

        return availableDoctors;
    }

    @Override
    public Schedule createScheduleWithFullTimeSlot(Doctor doctor, LocalDate date) {
        Schedule schedule = new Schedule();
        schedule.setDoctor(doctor);
        schedule.setDate(date);
        scheduleRepository.save(schedule);

        // For each timeslot enum, create a doctorTimeslotCapacity
        for (TimeSlot slot : TimeSlot.values()) {
            DoctorTimeslotCapacity capacity = new DoctorTimeslotCapacity();
            capacity.setTimeSlot(slot);
            capacity.setMaxPatients(2);
            capacity.setCurrentPatients(0);
            capacity.setSchedule(schedule);
            doctorTimeSlotCapacityRepository.save(capacity);
        }
        return schedule;
    }
}
