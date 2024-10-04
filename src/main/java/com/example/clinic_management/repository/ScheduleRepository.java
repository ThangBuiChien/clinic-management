package com.example.clinic_management.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.clinic_management.entities.Schedule;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Schedule findByDoctorIdAndDate(Long doctorId, LocalDate date);
}
