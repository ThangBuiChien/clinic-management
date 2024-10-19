package com.example.clinic_management.service.impl;

import com.example.clinic_management.dtos.requests.MedicalBillRequestDTO;
import com.example.clinic_management.dtos.responses.MedicalBillResponseDTO;
import com.example.clinic_management.entities.MedicalBill;
import com.example.clinic_management.entities.Patient;
import com.example.clinic_management.entities.PrescribedDrug;
import com.example.clinic_management.repository.MedicalBillRepository;
import com.example.clinic_management.repository.PatientRepository;
import com.example.clinic_management.repository.PrescribedDrugRepository;
import com.example.clinic_management.service.MedicalBillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicalBillServiceImpl implements MedicalBillService {
    private final MedicalBillRepository medicalBillRepository;
    private final PatientRepository patientRepository;
    private final PrescribedDrugRepository prdRepository;



    public MedicalBillResponseDTO createMedicalBill(MedicalBillRequestDTO medicalBillRequestDTO) {
        // 1. Validate patient exists
        Patient patient = patientRepository.findById(medicalBillRequestDTO.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

        // 2. Check if prescription exists for the symptom
        Optional<PrescribedDrug> existingPrecription = prdRepository
                .findBySymptomName(medicalBillRequestDTO.getSymptomName());

        MedicalBill medicalBill = new MedicalBill();

        if (existingPrecription.isPresent()) {
            // Use existing prescription
            PrescribedDrug prescribedDrug = existingPrecription.get();
            setMedicalBillFromExistingPrescription(medicalBill, patient, prescribedDrug, medicalBillRequestDTO);
        } else {
            // Validate new prescription data
            validateNewPrescriptionFields(medicalBillRequestDTO);
            setMedicalBillFromNewPresciption(medicalBill, patient, medicalBillRequestDTO);
        }

        MedicalBill savedMedicalBill = medicalBillRepository.save(medicalBill);
        return convertToResponseDTO(savedMedicalBill);
    }

    private void validateNewPrescriptionFields(MedicalBillRequestDTO requestDTO) {
        StringBuilder errorMessage = new StringBuilder();

        if (requestDTO.getSymptomName() == null || requestDTO.getSymptomName().trim().isEmpty()) {
            errorMessage.append("Symptom name is required. ");
        }

        if (requestDTO.getDosage() == null || requestDTO.getDosage().trim().isEmpty()) {
            errorMessage.append("Dosage is required for new prescriptions. ");
        }

        if (requestDTO.getSpecialInstructions() == null || requestDTO.getSpecialInstructions().trim().isEmpty()) {
            errorMessage.append("Special instructions are required for new prescriptions. ");
        }

        if (!errorMessage.isEmpty()) {
            throw new IllegalArgumentException(errorMessage.toString().trim());
        }
    }

    @Override
    public List<MedicalBillResponseDTO> getAllMedicalBills() {
        List<MedicalBill> medicalBills = medicalBillRepository.findAll();
        return medicalBills.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }


    private void setMedicalBillFromExistingPrescription(MedicalBill medicalBill, Patient patient, PrescribedDrug prescribedDrug, MedicalBillRequestDTO medicalBillRequestDTO) {
        medicalBill.setPatient(patient);
        medicalBill.setPatientName(patient.getFullName());
        medicalBill.setPatientDob(String.valueOf(patient.getBirthDate()));
        medicalBill.setPatientGender(String.valueOf(patient.getGender()));

        medicalBill.setSymptomName(prescribedDrug.getSymptomName());
        medicalBill.setSyndrome(medicalBillRequestDTO.getSyndrome());
//        medicalBill.setDrugName(prescribedDrug.getDrugs());
        medicalBill.setDosage(prescribedDrug.getDosage());
        medicalBill.setSpecialInstructions(prescribedDrug.getSpecialInstructions());
    }

    private void setMedicalBillFromNewPresciption(MedicalBill medicalBill, Patient patient, MedicalBillRequestDTO medicalBillRequestDTO) {

        medicalBill.setPatient(patient);
        medicalBill.setPatientName(patient.getFullName());
        medicalBill.setPatientDob(String.valueOf(patient.getBirthDate()));
        medicalBill.setPatientGender(String.valueOf(patient.getGender()));

        medicalBill.setSymptomName(medicalBillRequestDTO.getSymptomName());
//        medicalBill.setDrugName(medicalBillRequestDTO.getDrugName());
        medicalBill.setDosage(medicalBillRequestDTO.getDosage());
        medicalBill.setSpecialInstructions(medicalBillRequestDTO.getSpecialInstructions());
        medicalBill.setSyndrome(medicalBillRequestDTO.getSyndrome());
    }

    private MedicalBillResponseDTO convertToResponseDTO(MedicalBill medicalBill) {
        MedicalBillResponseDTO medicalBillResponseDTO = new MedicalBillResponseDTO();
        medicalBillResponseDTO.setId(medicalBill.getId());
        medicalBillResponseDTO.setPatientName(medicalBill.getPatientName());
        medicalBillResponseDTO.setPatientDoB(medicalBill.getPatientDob());
        medicalBillResponseDTO.setPatientGender(medicalBill.getPatientGender());
        medicalBillResponseDTO.setSymptomName(medicalBill.getSymptomName());
        medicalBillResponseDTO.setSyndrome(medicalBill.getSyndrome());
//        medicalBillResponseDTO.setDrugName(medicalBill.getDrugName());
        medicalBillResponseDTO.setDosage(medicalBill.getDosage());
        medicalBillResponseDTO.setSpecialInstructions(medicalBill.getSpecialInstructions());
        return medicalBillResponseDTO;
    }
}
