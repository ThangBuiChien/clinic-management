package com.example.clinic_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.clinic_management.entities.Symptom;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SymptomRepository extends JpaRepository<Symptom, Long> {
    Optional<Symptom> findByName(String name);

    @Query("SELECT s FROM Symptom s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Symptom> findByNameContainingIgnoreCase(@Param("name") String name, Pageable pageable);

}
