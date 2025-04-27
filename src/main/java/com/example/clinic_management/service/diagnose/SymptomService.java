package com.example.clinic_management.service.diagnose;

import java.util.List;

import com.example.clinic_management.dtos.requests.SymptomNameResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.clinic_management.dtos.requests.SymptomRequestDTO;
import com.example.clinic_management.dtos.responses.SymptomResponseDTO;

public interface SymptomService {
    List<SymptomResponseDTO> getAllSymptoms();

    Page<SymptomResponseDTO> getAllSymptoms(Pageable pageable);

    SymptomResponseDTO getSymptomById(Long id);

    SymptomResponseDTO addSymptom(SymptomRequestDTO symptomRequestDTO);

    SymptomResponseDTO updateSymptom(Long id, SymptomRequestDTO symptomRequestDTO);

    void deleteSymptom(Long id);

    SymptomResponseDTO createSymptomWithDrugs(SymptomRequestDTO symptomRequestDTO);

    SymptomResponseDTO getSymptomByName(String name);

    List<SymptomNameResponseDTO> getAllSymptomNames();
}
