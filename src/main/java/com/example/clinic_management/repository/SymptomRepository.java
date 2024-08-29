package com.example.clinic_management.repository;

import com.example.clinic_management.entities.Symptom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SymptomRepository extends JpaRepository<Symptom, Long> {
    Symptom findByName(String name);
}
