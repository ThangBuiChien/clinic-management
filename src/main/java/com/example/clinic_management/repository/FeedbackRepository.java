package com.example.clinic_management.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.clinic_management.entities.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    Page<Feedback> findAllByDoctorId(Long doctorId, Pageable pageable);

    Page<Feedback> findAllByPatientId(Long patientId, Pageable pageable);
}
