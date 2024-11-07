package com.example.clinic_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.clinic_management.entities.ExaminationDetail;

public interface ExaminationRepository extends JpaRepository<ExaminationDetail, Long> {}
