package com.example.clinic_management.service;

import java.time.LocalDate;
import java.util.List;

import com.example.clinic_management.entities.Doctor;
import com.example.clinic_management.enums.TimeSlot;

public interface BookingProcessor {
    boolean checkAvailability(Long doctorId, LocalDate date, TimeSlot timeSlot);

    List<Doctor> findAvailableDoctors(LocalDate date, TimeSlot timeSlot);
}
