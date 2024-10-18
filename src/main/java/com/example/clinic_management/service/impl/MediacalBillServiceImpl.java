package com.example.clinic_management.service.impl;

import com.example.clinic_management.dtos.requests.MedicalBillRequestDTO;
import com.example.clinic_management.dtos.responses.MedicalBillResponseDTO;
import com.example.clinic_management.entities.MedicalBill;
import com.example.clinic_management.entities.Patient;
import com.example.clinic_management.entities.PrescribedDrug;
import com.example.clinic_management.exception.PatientNotFoundException;
import com.example.clinic_management.exception.PrescriptionValidationException;
import com.example.clinic_management.repository.MedicalBillRepository;
import com.example.clinic_management.repository.PatientRepository;
import com.example.clinic_management.repository.PrescribedDrugRepository;
import com.example.clinic_management.service.MedicalBillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicalBillServiceImpl implements MedicalBillService {
    private final MedicalBillRepository medicalBillRepository;
    private final PrescribedDrugRepository prdRepository;
    private final PatientRepository patientRepository;

    @Override
    @Transactional
    public MedicalBillResponseDTO createMedicalBill(MedicalBillRequestDTO medicalBillRequestDTO) {
        // Find the patient by ID
        Patient patient = patientRepository.findById(medicalBillRequestDTO.getPatientId())
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with ID: " + medicalBillRequestDTO.getPatientId()));

        MedicalBill medicalBill = new MedicalBill();

        // Set patient reference
        medicalBill.setPatient(patient);
        medicalBill.setSymptomName(medicalBillRequestDTO.getSymptomName());

        // Try to find prescription in prescribe table
        PrescribedDrug prescribe = prdRepository.findBySymptomName(medicalBillRequestDTO.getSymptomName())
                .orElseGet(() -> {
                    validateNewPrescription(medicalBillRequestDTO);
//            medicalBill.setSyndrome(prescribe.getSyndrome());
            medicalBill.setDrugName(medicalBillRequestDTO.getDrugName());
            medicalBill.setDosage(medicalBillRequestDTO.getDosage());
            medicalBill.setSpecialInstructions(medicalBillRequestDTO.getSpecialInstructions());
            return null;
        });
        if (prescribe != null) {
            // Validate all required fields for new prescription
            validateNewPrescription(medicalBillRequestDTO);

            // Use new prescription data provided by doctor
            medicalBill.setSyndrome(medicalBillRequestDTO.getSymptomName());
            medicalBill.setDosage(medicalBillRequestDTO.getDosage());
            medicalBill.setDrugName(medicalBillRequestDTO.getDrugName());
            medicalBill.setSpecialInstructions(medicalBillRequestDTO.getSpecialInstructions());

        }
        // Save to medical_bill table
        MedicalBill savedMedicalBill = medicalBillRepository.save(medicalBill);
        // Convert to response DTO
        return convertToResponseDTO(savedMedicalBill);
    }

    private void validateNewPrescription(MedicalBillRequestDTO medicalBillRequestDTO) {
        StringBuilder errorMessage = new StringBuilder();

//        if (!StringUtils.hasText(medicalBillRequestDTO.getSyndrome())) {
//            errorMessage.append("Syndrome is required for new prescription. ");
//        }

        if (medicalBillRequestDTO.getDrugName() == null || medicalBillRequestDTO.getDrugName().isEmpty()) {
            errorMessage.append("Drug name list is required for new prescription. ");
        } else {
            for (String drug: medicalBillRequestDTO.getDrugName()) {
                if (!StringUtils.hasText(drug)) {
                    errorMessage.append("Drug names cannot be blank. ");
                    break;
                }
            }
        }

        if (!StringUtils.hasText(medicalBillRequestDTO.getDosage())) {
            errorMessage.append("Dosage is required for new prescription. ");
        }

        if (!StringUtils.hasText(medicalBillRequestDTO.getSpecialInstructions())) {
            errorMessage.append("Special instructions is required for new prescription. ");
        }

        if (!errorMessage.isEmpty()) {
            throw new PrescriptionValidationException(errorMessage.toString().trim());
        }

    }

    private MedicalBillResponseDTO convertToResponseDTO(MedicalBill medicalBill) {
        MedicalBillResponseDTO medicalBillResponseDTO = new MedicalBillResponseDTO();
        medicalBillResponseDTO.setId(medicalBill.getId());
        medicalBillResponseDTO.setPatientName(medicalBill.getPatient().getFullName());
        medicalBillResponseDTO.setPatientDoB(medicalBill.getPatient().getBirthDate().toString());
        medicalBillResponseDTO.setPatientGender(medicalBill.getPatient().getGender().toString());
        medicalBillResponseDTO.setSymptomName(medicalBill.getSymptomName());
//        medicalBillResponseDTO.setSyndrome(medicalBill.getSyndrome());
        medicalBillResponseDTO.setDrugName(medicalBill.getDrugName());
        medicalBillResponseDTO.setDosage(medicalBill.getDosage());
        medicalBillResponseDTO.setSpecialInstructions(medicalBill.getSpecialInstructions());
        return medicalBillResponseDTO;
    }
}
