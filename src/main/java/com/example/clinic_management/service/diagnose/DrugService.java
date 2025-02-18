package com.example.clinic_management.service.diagnose;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.clinic_management.dtos.requests.DrugRequestDTO;
import com.example.clinic_management.dtos.responses.DrugResponseDTO;

public interface DrugService {
    List<DrugResponseDTO> getAllDrugs();

    Page<DrugResponseDTO> getAllDrugs(Pageable pageable);

    DrugResponseDTO getDrugById(Long id);

    DrugResponseDTO addDrug(DrugRequestDTO drugResponseDTO);

    DrugResponseDTO updateDrug(Long id, DrugRequestDTO drugResponseDTO);

    void deleteDrug(Long id);
}
