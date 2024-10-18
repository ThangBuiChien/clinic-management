package com.example.clinic_management.repository;

import com.example.clinic_management.entities.Drug;
import com.example.clinic_management.entities.PrescribedDrug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrescribedDrugRepository extends JpaRepository<PrescribedDrug, Long> {
    Optional<PrescribedDrug> findSymptomName(String symptomName);
}