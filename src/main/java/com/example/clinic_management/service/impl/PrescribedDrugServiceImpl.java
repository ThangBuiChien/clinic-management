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
import com.example.clinic_management.mapper.AutoPrescribedDrugMapper; // Assuming you have a mapper for PrescribedDrug
import com.example.clinic_management.repository.DrugRepository; // Assuming you have a Drug repository
import com.example.clinic_management.repository.PrescribedDrugRepository;
import com.example.clinic_management.service.PrescribedDrugService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrescribedDrugServiceImpl implements PrescribedDrugService {

    private final PrescribedDrugRepository prescribedDrugRepository;
    private final DrugRepository drugRepository; // Injecting DrugRepository
    private final AutoPrescribedDrugMapper autoPrescribedDrugMapper; // Mapper for PrescribedDrug

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
        // Validate and retrieve drugs from the repository
        List<Drug> drugs = drugRepository.findAllById(prescribedDrugRequestDTO.getDrugIds());
        if (drugs.size() != prescribedDrugRequestDTO.getDrugIds().size()) {
            throw new ResourceNotFoundException("Some drugs not found");
        }

        // Create and save new PrescribedDrug instances
        for (Drug drug : drugs) {
            PrescribedDrug newPrescribedDrug = new PrescribedDrug();
            newPrescribedDrug.setDrug(drug);
            newPrescribedDrug.setSymtomName(prescribedDrugRequestDTO.getSymtomName());
            newPrescribedDrug.setDosage(prescribedDrugRequestDTO.getDosage());
            newPrescribedDrug.setSpecialInstructions(prescribedDrugRequestDTO.getSpecialInstructions());

            prescribedDrugRepository.save(newPrescribedDrug);
        }

        // Return the response for the first saved prescribed drug (or adjust as needed)
        return autoPrescribedDrugMapper.toResponseDTO(prescribedDrugRepository.findAll().get(0));
    }

    @Override
    public PrescribedDrugResponseDTO updatePrescribedDrug(Long id, PrescribedDrugRequestDTO prescribedDrugRequestDTO) {
        PrescribedDrug oldPrescribedDrug = prescribedDrugRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PrescribedDrug", "id", id));


        oldPrescribedDrug.setSymtomName(prescribedDrugRequestDTO.getSymtomName());
        oldPrescribedDrug.setDosage(prescribedDrugRequestDTO.getDosage());
        oldPrescribedDrug.setSpecialInstructions(prescribedDrugRequestDTO.getSpecialInstructions());



        prescribedDrugRepository.save(oldPrescribedDrug);
        return autoPrescribedDrugMapper.toResponseDTO(oldPrescribedDrug);
    }

    @Override
    public void deletePrescribedDrug(Long id) {
        prescribedDrugRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PrescribedDrug", "id", id));
        prescribedDrugRepository.deleteById(id);
    }
}