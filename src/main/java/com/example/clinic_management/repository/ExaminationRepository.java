package com.example.clinic_management.repository;

import com.example.clinic_management.entities.ExaminationDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExaminationRepository extends JpaRepository<ExaminationDetail, Long> {
}
