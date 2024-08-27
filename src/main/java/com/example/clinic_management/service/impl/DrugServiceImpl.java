package com.example.clinic_management.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.clinic_management.dtos.requests.DrugRequestDTO;
import com.example.clinic_management.dtos.responses.DrugResponseDTO;
import com.example.clinic_management.entities.Drug;
import com.example.clinic_management.exception.ResourceNotFoundException;
import com.example.clinic_management.mapper.AutoDrugMapper;
import com.example.clinic_management.repository.DrugRepository;
import com.example.clinic_management.service.DrugService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DrugServiceImpl implements DrugService {

    private final DrugRepository drugRepository;

    private final AutoDrugMapper autoDrugMapper;

    @Override
    public List<DrugResponseDTO> getAllDrugs() {
        return drugRepository.findAll().stream()
                .map(autoDrugMapper::toResponseDTO)
                .toList();
    }

    @Override
    public Page<DrugResponseDTO> getAllDrugs(Pageable pageable) {
        Page<Drug> drugs = drugRepository.findAll(pageable);
        List<DrugResponseDTO> drugDTOs =
                drugs.stream().map(autoDrugMapper::toResponseDTO).toList();
        return new PageImpl<>(drugDTOs, pageable, drugs.getTotalElements());
    }

    @Override
    public DrugResponseDTO getDrugById(Long id) {
        return drugRepository
                .findById(id)
                .map(autoDrugMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Drug", "id", id));
    }

    @Override
    public DrugResponseDTO addDrug(DrugRequestDTO drugResponseDTO) {
        Drug newDrug = autoDrugMapper.toEntity(drugResponseDTO);
        drugRepository.save(newDrug);
        return autoDrugMapper.toResponseDTO(newDrug);
    }

    @Override
    public DrugResponseDTO updateDrug(Long id, DrugRequestDTO drugResponseDTO) {
        Drug oldDrug = drugRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Drug", "id", id));
        Drug newDrug = autoDrugMapper.toEntity(drugResponseDTO);
        newDrug.setId(oldDrug.getId());
        drugRepository.save(newDrug);
        return autoDrugMapper.toResponseDTO(newDrug);
    }

    @Override
    public void deleteDrug(Long id) {

    }
}
