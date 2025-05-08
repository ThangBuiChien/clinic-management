package com.example.clinic_management.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.clinic_management.entities.Drug;

@Repository
public interface DrugRepository extends JpaRepository<Drug, Long> {
    Drug findByName(String name);

    Page<Drug> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

}
