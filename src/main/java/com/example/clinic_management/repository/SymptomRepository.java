package com.example.clinic_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.clinic_management.entities.Symptom;

public interface SymptomRepository extends JpaRepository<Symptom, Long> {
    Symptom findByName(String name);
}
