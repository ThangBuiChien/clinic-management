package com.example.clinic_management.service;

import com.example.clinic_management.dtos.requests.PrescribedDrugRequestDTO;
import com.example.clinic_management.dtos.responses.PrescribedDrugResponseDTO; // Import the DTO
import com.example.clinic_management.entities.PrescribedDrug;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PrescribedDrugService {
    List<PrescribedDrugResponseDTO> getAllPrescribedDrugs(); // Updated return type
    Page<PrescribedDrugResponseDTO> getAllPrescribedDrugs(Pageable pageable);
    PrescribedDrugResponseDTO getPrescribedDrugById(Long id); // Updated return type
    PrescribedDrugResponseDTO addPrescribedDrug(PrescribedDrugRequestDTO prescribedDrugRequestDTO);
    PrescribedDrugResponseDTO updatePrescribedDrug(Long id, PrescribedDrugRequestDTO prescribedDrugRequestDTO);
    void deletePrescribedDrug(Long id);
}