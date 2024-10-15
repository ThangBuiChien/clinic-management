package com.example.clinic_management.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.clinic_management.dtos.requests.PrescribedDrugRequestDTO;
import com.example.clinic_management.dtos.responses.PrescribedDrugResponseDTO;
import com.example.clinic_management.entities.PrescribedDrug;
import com.example.clinic_management.exception.ResourceNotFoundException;
import com.example.clinic_management.mapper.AutoPrescribedDrugMapper; // Assuming you have a mapper for PrescribedDrug
import com.example.clinic_management.repository.PrescribedDrugRepository;
import com.example.clinic_management.service.PrescribedDrugService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrescribedDrugServiceImpl implements PrescribedDrugService {

    private final PrescribedDrugRepository prescribedDrugRepository;
    private final AutoPrescribedDrugMapper autoPrescribedDrugMapper; // Mapper for PrescribedDrug

    public boolean isTemplateDuplicate(String templateName) {
        List<PrescribedDrug> prescribedDrugs = prescribedDrugRepository.findByTemplateName(templateName);
        return !prescribedDrugs.isEmpty(); // Returns true if duplicates exist
    }

    @Override
    public List<PrescribedDrugResponseDTO> getAllPrescribedDrugs() {
        return prescribedDrugRepository.findAll().stream()
                .map(autoPrescribedDrugMapper::toResponseDTO)
                .toList();
    }

    @Override
    public Page<PrescribedDrugResponseDTO> getAllPrescribedDrugs(Pageable pageable) {
        Page<PrescribedDrug> prescribedDrugs = prescribedDrugRepository.findAll(pageable);
        List<PrescribedDrugResponseDTO> prescribedDrugDTOs =
                prescribedDrugs.stream().map(autoPrescribedDrugMapper::toResponseDTO).toList();
        return new PageImpl<>(prescribedDrugDTOs, pageable, prescribedDrugs.getTotalElements());
    }

    @Override
    public PrescribedDrugResponseDTO getPrescribedDrugById(Long id) {
        return prescribedDrugRepository
                .findById(id)
                .map(autoPrescribedDrugMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("PrescribedDrug", "id", id));
    }

    @Override
    public PrescribedDrugResponseDTO addPrescribedDrug(PrescribedDrugRequestDTO prescribedDrugRequestDTO) {
        PrescribedDrug newPrescribedDrug = autoPrescribedDrugMapper.toEntity(prescribedDrugRequestDTO);
        prescribedDrugRepository.save(newPrescribedDrug);
        return autoPrescribedDrugMapper.toResponseDTO(newPrescribedDrug);
    }

    @Override
    public PrescribedDrugResponseDTO updatePrescribedDrug(Long id, PrescribedDrugRequestDTO prescribedDrugRequestDTO) {
        PrescribedDrug oldPrescribedDrug = prescribedDrugRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PrescribedDrug", "id", id));
        PrescribedDrug updatedPrescribedDrug = autoPrescribedDrugMapper.toEntity(prescribedDrugRequestDTO);
        updatedPrescribedDrug.setId(oldPrescribedDrug.getId());
        prescribedDrugRepository.save(updatedPrescribedDrug);
        return autoPrescribedDrugMapper.toResponseDTO(updatedPrescribedDrug);
    }

    @Override
    public void deletePrescribedDrug(Long id) {
        prescribedDrugRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PrescribedDrug", "id", id));
        prescribedDrugRepository.deleteById(id);
    }
}