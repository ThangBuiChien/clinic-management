package com.example.clinic_management.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.example.clinic_management.entities.PrescribedDrug;

@Repository
public interface PrescribedDrugRepository extends JpaRepository<PrescribedDrug, Long> {
    @NonNull
    Optional<PrescribedDrug> findById(@NonNull Long id);
}
