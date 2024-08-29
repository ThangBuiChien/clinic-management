package com.example.clinic_management.service;



import com.example.clinic_management.dtos.requests.SymptomRequestDTO;
import com.example.clinic_management.dtos.responses.SymptomResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SymptomService {
    List<SymptomResponseDTO> getAllSymptoms();

    Page<SymptomResponseDTO> getAllSymptoms(Pageable pageable);

    SymptomResponseDTO getSymptomById(Long id);

    SymptomResponseDTO addSymptom(SymptomRequestDTO symptomRequestDTO);

    SymptomResponseDTO updateSymptom(Long id, SymptomRequestDTO symptomRequestDTO);

    void deleteSymptom(Long id);
}
