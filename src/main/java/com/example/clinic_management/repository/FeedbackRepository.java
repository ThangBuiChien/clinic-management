package com.example.clinic_management.repository;

import com.example.clinic_management.entities.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    Page<Feedback> findAllByDoctorId(Long doctorId, Pageable pageable);
}
