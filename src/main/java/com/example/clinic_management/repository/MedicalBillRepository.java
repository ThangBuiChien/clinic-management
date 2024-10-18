package com.example.clinic_management.repository;

import com.example.clinic_management.entities.MedicalBill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalBillRepository extends JpaRepository<MedicalBill, Long> {

}
