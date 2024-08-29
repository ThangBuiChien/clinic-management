package com.example.clinic_management.service.impl;

import com.example.clinic_management.dtos.requests.SymptomRequestDTO;
import com.example.clinic_management.dtos.responses.SymptomResponseDTO;
import com.example.clinic_management.entities.Symptom;
import com.example.clinic_management.exception.ResourceNotFoundException;
import com.example.clinic_management.mapper.AutoSymptomMapper;
import com.example.clinic_management.repository.SymptomRepository;
import com.example.clinic_management.service.SymptomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SymptomServiceImpl implements SymptomService {
    private final SymptomRepository symptomRepository;
    private final AutoSymptomMapper autoSymptomMapper;

    @Override
    public List<SymptomResponseDTO> getAllSymptoms() {
        return symptomRepository.findAll().stream()
                .map(autoSymptomMapper::toResponseDTO)
                .toList();
    }

    @Override
    public Page<SymptomResponseDTO> getAllSymptoms(Pageable pageable) {
        Page<Symptom> symptoms = symptomRepository.findAll(pageable);
        List<SymptomResponseDTO> symptomDTOs =
                symptoms.stream().map(autoSymptomMapper::toResponseDTO).toList();
        return new PageImpl<>(symptomDTOs, pageable, symptoms.getTotalElements());
    }

    @Override
    public SymptomResponseDTO getSymptomById(Long id) {
        return symptomRepository
                .findById(id)
                .map(autoSymptomMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Symptom", "id", id));
    }

    @Override
    public SymptomResponseDTO addSymptom(SymptomRequestDTO symptomRequestDTO) {
        Symptom newSymptom = autoSymptomMapper.toEntity(symptomRequestDTO);
        symptomRepository.save(newSymptom);
        return autoSymptomMapper.toResponseDTO(newSymptom);
    }

    @Override
    public SymptomResponseDTO updateSymptom(Long id, SymptomRequestDTO symptomRequestDTO) {
        Symptom oldSymptom = symptomRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Symptom", "id", id));
        Symptom newSymptom = autoSymptomMapper.toEntity(symptomRequestDTO);
        newSymptom.setId(oldSymptom.getId());
        symptomRepository.save(newSymptom);
        return autoSymptomMapper.toResponseDTO(newSymptom);
    }

    @Override
    public void deleteSymptom(Long id) {
        symptomRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Symptom", "id", id));
        symptomRepository.deleteById(id);
    }


























}