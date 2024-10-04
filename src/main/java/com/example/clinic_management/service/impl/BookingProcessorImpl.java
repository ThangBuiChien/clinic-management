package com.example.clinic_management.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.clinic_management.entities.Doctor;
import com.example.clinic_management.entities.DoctorTimeslotCapacity;
import com.example.clinic_management.entities.Schedule;
import com.example.clinic_management.enums.TimeSlot;
import com.example.clinic_management.exception.ResourceNotFoundException;
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
            schedule = new Schedule();
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
    public List<Doctor> findAvailableDoctors(LocalDate date, TimeSlot timeSlot) {
        return null;
    }
}
