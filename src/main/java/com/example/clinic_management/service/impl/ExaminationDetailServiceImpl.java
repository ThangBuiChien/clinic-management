package com.example.clinic_management.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.example.clinic_management.dtos.requests.ExaminationRequestDTO;
import com.example.clinic_management.entities.ExaminationDetail;
import com.example.clinic_management.entities.Patient;
import com.example.clinic_management.repository.ExaminationRepository;
import com.example.clinic_management.repository.PatientRepository;
import com.example.clinic_management.service.ExaminationDetailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExaminationDetailServiceImpl implements ExaminationDetailService {

    private final ExaminationRepository examinationRepository;
    private final PatientRepository patientRepository;

    @Override
    @Transactional
    public ExaminationDetail createExaminationDetail(ExaminationRequestDTO examinationRequestDTO) {

        Patient patient = validateAndGetExaminationDetail(examinationRequestDTO);
        ExaminationDetail examinationDetail = new ExaminationDetail();
        //        examinationDetail.setPatient(patient);
        examinationDetail.setPatientName(patient.getFullName());
        examinationDetail.setExaminationType(
                examinationRequestDTO.getExaminationType().trim());
        examinationDetail.setExaminationResult(
                examinationRequestDTO.getExaminationResult().trim());
        return examinationRepository.save(examinationDetail);
    }

    @Override
    @Transactional
    public ExaminationDetail getExaminationDetailById(Long id) {
        return examinationRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Examination not found"));
    }

    @Override
    @Transactional
    public List<ExaminationDetail> getAllExaminationDetails() {
        return examinationRepository.findAll();
    }

    private Patient validateAndGetExaminationDetail(ExaminationRequestDTO examinationRequestDTO) {
        // First check if patient ID exists
        if (examinationRequestDTO.getPatientId() == null) {
            throw new IllegalArgumentException("Patient ID is required");
        }

        // Validate patient existence
        Patient patient = patientRepository
                .findById(examinationRequestDTO.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Patient not found with id: " + examinationRequestDTO.getPatientId()));

        // If patient exists, both examination type and result must be filled
        if (!StringUtils.hasText(examinationRequestDTO.getExaminationType())
                || examinationRequestDTO.getExaminationType().trim().isEmpty()) {
            throw new IllegalArgumentException("Examination type is required when patient ID is provided");
        }

        if (!StringUtils.hasText(examinationRequestDTO.getExaminationResult())
                || examinationRequestDTO.getExaminationResult().trim().isEmpty()) {
            throw new IllegalArgumentException("Examination result is required when patient ID is provided");
        }
        return patient;
    }
}
