package com.example.clinic_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.clinic_management.entities.DoctorTimeslotCapacity;
import com.example.clinic_management.enums.TimeSlot;

@Repository
public interface DoctorTimeSlotCapacityRepository extends JpaRepository<DoctorTimeslotCapacity, Long> {
    DoctorTimeslotCapacity findByScheduleIdAndTimeSlot(Long scheduleId, TimeSlot timeSlot);
}
