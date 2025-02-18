package com.example.clinic_management.service.diagnose;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.clinic_management.dtos.requests.PrescribedDrugRequestDTO;
import com.example.clinic_management.dtos.responses.PrescribedDrugResponseDTO;

public interface PrescribedDrugService {
    List<PrescribedDrugResponseDTO> getAllPrescribedDrugs();

    Page<PrescribedDrugResponseDTO> getAllPrescribedDrugs(Pageable pageable);

    PrescribedDrugResponseDTO getPrescribedDrugById(Long id);

    PrescribedDrugResponseDTO addPrescribedDrug(PrescribedDrugRequestDTO prescribedDrugRequestDTO);

    PrescribedDrugResponseDTO updatePrescribedDrug(Long id, PrescribedDrugRequestDTO prescribedDrugRequestDTO);

    void deletePrescribedDrug(Long id);
}
