package com.example.clinic_management.repository;

import com.example.clinic_management.entities.MedicalBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MedicalBillRepository extends JpaRepository<MedicalBill, Long> {
    List<MedicalBill> findByPatientFullNameContainingIgnoreCase(String patientName);
    List<MedicalBill> findByDoctorFullNameContainingIgnoreCase(String doctorName);
    @Query("SELECT DISTINCT mb FROM MedicalBill mb LEFT JOIN FETCH mb.drugs d LEFT JOIN FETCH d.drug")
    List<MedicalBill> findAllWithPrescribedDrugs();

    @Query("SELECT DISTINCT mb FROM MedicalBill mb LEFT JOIN FETCH mb.drugs d LEFT JOIN FETCH d.drug WHERE mb.id = :id")
    Optional<MedicalBill> findByIdWithPrescribedDrugs(@Param("id") Long id);

}