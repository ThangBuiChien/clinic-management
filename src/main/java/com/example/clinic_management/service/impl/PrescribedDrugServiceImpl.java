package com.example.clinic_management.service.impl;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.clinic_management.dtos.requests.PrescribedDrugRequestDTO;
import com.example.clinic_management.dtos.responses.PrescribedDrugResponseDTO;
import com.example.clinic_management.entities.Drug;
import com.example.clinic_management.entities.PrescribedDrug;
import com.example.clinic_management.exception.ResourceNotFoundException;
import com.example.clinic_management.mapper.AutoPrescribedDrugMapper;
import com.example.clinic_management.repository.DrugRepository;
import com.example.clinic_management.repository.PrescribedDrugRepository;
import com.example.clinic_management.service.PrescribedDrugService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrescribedDrugServiceImpl implements PrescribedDrugService {

    private final PrescribedDrugRepository prescribedDrugRepository;
    private final DrugRepository drugRepository;
    private final AutoPrescribedDrugMapper autoPrescribedDrugMapper;

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
        Drug drug = drugRepository.findById(prescribedDrugRequestDTO.getDrugId())
                .orElseThrow(() -> new ResourceNotFoundException("Drug", "id", prescribedDrugRequestDTO.getDrugId()));

        PrescribedDrug newPrescribedDrug = new PrescribedDrug();
        newPrescribedDrug.setDrug(drug);
        newPrescribedDrug.setDosage(prescribedDrugRequestDTO.getDosage());
        newPrescribedDrug.setDuration(prescribedDrugRequestDTO.getDuration());
        newPrescribedDrug.setFrequency(prescribedDrugRequestDTO.getFrequency());
        newPrescribedDrug.setSpecialInstructions(prescribedDrugRequestDTO.getSpecialInstructions());

        prescribedDrugRepository.save(newPrescribedDrug);

        return autoPrescribedDrugMapper.toResponseDTO(newPrescribedDrug);
    }

    @Override
    public PrescribedDrugResponseDTO updatePrescribedDrug(Long id, PrescribedDrugRequestDTO prescribedDrugRequestDTO) {
        PrescribedDrug existingPrescribedDrug = prescribedDrugRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PrescribedDrug", "id", id));

        Drug drug = drugRepository.findById(prescribedDrugRequestDTO.getDrugId())
                .orElseThrow(() -> new ResourceNotFoundException("Drug", "id", prescribedDrugRequestDTO.getDrugId()));

        existingPrescribedDrug.setDrug(drug);
        existingPrescribedDrug.setDosage(prescribedDrugRequestDTO.getDosage());
        existingPrescribedDrug.setDuration(prescribedDrugRequestDTO.getDuration());
        existingPrescribedDrug.setFrequency(prescribedDrugRequestDTO.getFrequency());
        existingPrescribedDrug.setSpecialInstructions(prescribedDrugRequestDTO.getSpecialInstructions());

        prescribedDrugRepository.save(existingPrescribedDrug);

        return autoPrescribedDrugMapper.toResponseDTO(existingPrescribedDrug);
    }

    @Override
    public void deletePrescribedDrug(Long id) {
        prescribedDrugRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PrescribedDrug", "id", id));
        prescribedDrugRepository.deleteById(id);
    }
}