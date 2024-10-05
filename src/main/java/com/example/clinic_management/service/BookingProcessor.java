package com.example.clinic_management.service;

import java.time.LocalDate;
import java.util.List;

import com.example.clinic_management.entities.Doctor;
import com.example.clinic_management.entities.Schedule;
import com.example.clinic_management.enums.TimeSlot;

public interface BookingProcessor {
    boolean checkAvailability(Long doctorId, LocalDate date, TimeSlot timeSlot);

    List<Doctor> findAvailableDoctors(Long departmentId, LocalDate date, TimeSlot timeSlot);

    Schedule createScheduleWithFullTimeSlot(Doctor doctor, LocalDate date);
}
