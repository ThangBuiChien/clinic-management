package com.example.clinic_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.clinic_management.entities.Symptom;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface SymptomRepository extends JpaRepository<Symptom, Long> {
    Optional<Symptom> findByName(String name);
}
