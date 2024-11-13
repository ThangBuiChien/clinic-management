package com.example.clinic_management.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.clinic_management.dtos.requests.MedicalBillRequestDTO;
import com.example.clinic_management.dtos.responses.MedicalBillResponseDTO;

public interface MedicalBillService {
    MedicalBillResponseDTO createMedicalBill(MedicalBillRequestDTO medicalBillRequestDTO);

    MedicalBillResponseDTO getMedicalBillById(Long id);

    List<MedicalBillResponseDTO> getAllMedicalBills();

    Page<MedicalBillResponseDTO> getAllMedicalBills(Pageable pageable);

    List<MedicalBillResponseDTO> findMedicalBillsByPatientName(String patientName);

    List<MedicalBillResponseDTO> findMedicalBillsByDoctorName(String doctorName);

    MedicalBillResponseDTO updateMedicalBill(Long id, MedicalBillRequestDTO medicalBillRequestDTO);

    void deleteMedicalBill(Long id);
}
