package com.example.clinic_management.service.impl;

import com.example.clinic_management.dtos.requests.MedicalBillRequestDTO;
import com.example.clinic_management.dtos.responses.MedicalBillResponseDTO;
import com.example.clinic_management.entities.Drug;
import com.example.clinic_management.entities.MedicalBill;
import com.example.clinic_management.entities.Patient;
import com.example.clinic_management.entities.PrescribedDrug;
import com.example.clinic_management.repository.MedicalBillRepository;
import com.example.clinic_management.repository.PatientRepository;
import com.example.clinic_management.repository.PrescribedDrugRepository;
import com.example.clinic_management.service.MedicalBillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicalBillServiceImpl implements MedicalBillService {
    private final MedicalBillRepository medicalBillRepository;
    private final PatientRepository patientRepository;
    private final PrescribedDrugRepository prescribedDrugRepository;


    @Override
    @Transactional
    public MedicalBillResponseDTO createMedicalBill(MedicalBillRequestDTO medicalBillRequestDTO) {
        // 1. Validate patient exists
        Patient patient = patientRepository.findById(medicalBillRequestDTO.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

        // 2. Check if prescription exists for the symptom
        PrescribedDrug prescribedDrug;
        List<String> drugNames = new ArrayList<>(medicalBillRequestDTO.getDrugNames());

        if (medicalBillRequestDTO.getPrescribedDrugId() != null) {
            prescribedDrug = prescribedDrugRepository.findById(medicalBillRequestDTO.getPrescribedDrugId())
                    .orElseThrow(() -> new IllegalArgumentException("Prescribed drug not found"));

            // Merge drug names from the existing prescribed drug with the new input
            if (prescribedDrug.getDrugs() != null) {
                drugNames.addAll(prescribedDrug.getDrugs().stream()
                        .map(Drug::getName)
                        .filter(name -> !drugNames.contains(name))
                        .collect(Collectors.toList()));
            }
        } else {
            validateNewPrescriptionFields(medicalBillRequestDTO);
            prescribedDrug = createNewPrescribedDrug(medicalBillRequestDTO);
        }

        MedicalBill medicalBill = new MedicalBill();
        setMedicalBillFromPrescription(medicalBill, patient, prescribedDrug, medicalBillRequestDTO);

        // Set the merged drug names
        medicalBill.setDrugNames(drugNames);

        MedicalBill savedMedicalBill = medicalBillRepository.save(medicalBill);
        return convertToResponseDTO(savedMedicalBill);
    }

    private PrescribedDrug createNewPrescribedDrug(MedicalBillRequestDTO medicalBillRequestDTO) {
        PrescribedDrug newPrescribedDrug = new PrescribedDrug();
        newPrescribedDrug.setSymptomName(medicalBillRequestDTO.getSymptomName());
        newPrescribedDrug.setDosage(medicalBillRequestDTO.getDosage());
        newPrescribedDrug.setSpecialInstructions(medicalBillRequestDTO.getSpecialInstructions());
        return prescribedDrugRepository.save(newPrescribedDrug);
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

    @Override
    public MedicalBillResponseDTO getMedicalBillById(Long id) {
        MedicalBill medicalBill = medicalBillRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Medical bill not found with id: " + id));
        return convertToResponseDTO(medicalBill);
    }


//    private void setMedicalBillFromExistingPrescription(MedicalBill medicalBill, Patient patient, PrescribedDrug prescribedDrug, MedicalBillRequestDTO medicalBillRequestDTO) {
//        medicalBill.setPatient(patient);
//        medicalBill.setPatientName(patient.getFullName());
//        medicalBill.setPatientDob(String.valueOf(patient.getBirthDate()));
//        medicalBill.setPatientGender(String.valueOf(patient.getGender()));
//
//        medicalBill.setSymptomName(prescribedDrug.getSymptomName());
//        medicalBill.setSyndrome(medicalBillRequestDTO.getSyndrome());
////        medicalBill.setDrugName(prescribedDrug.getDrugs());
//        medicalBill.setDosage(prescribedDrug.getDosage());
//        medicalBill.setSpecialInstructions(prescribedDrug.getSpecialInstructions());
//    }
//
//    private void setMedicalBillFromNewPresciption(MedicalBill medicalBill, Patient patient, MedicalBillRequestDTO medicalBillRequestDTO) {
//
//        medicalBill.setPatient(patient);
//        medicalBill.setPatientName(patient.getFullName());
//        medicalBill.setPatientDob(String.valueOf(patient.getBirthDate()));
//        medicalBill.setPatientGender(String.valueOf(patient.getGender()));
//
//        medicalBill.setSymptomName(medicalBillRequestDTO.getSymptomName());
////        medicalBill.setDrugName(medicalBillRequestDTO.getDrugName());
//        medicalBill.setDosage(medicalBillRequestDTO.getDosage());
//        medicalBill.setSpecialInstructions(medicalBillRequestDTO.getSpecialInstructions());
//        medicalBill.setSyndrome(medicalBillRequestDTO.getSyndrome());
//    }
private void setMedicalBillFromPrescription(MedicalBill medicalBill, Patient patient, PrescribedDrug prescribedDrug, MedicalBillRequestDTO medicalBillRequestDTO) {
    medicalBill.setPatient(patient);
    medicalBill.setPatientName(patient.getFullName());
    medicalBill.setPatientDob(String.valueOf(patient.getBirthDate()));
    medicalBill.setPatientGender(String.valueOf(patient.getGender()));

    medicalBill.setPrescribedDrug(prescribedDrug);
    medicalBill.setSymptomName(prescribedDrug.getSymptomName());
    medicalBill.setSyndrome(medicalBillRequestDTO.getSyndrome());
    medicalBill.setDosage(prescribedDrug.getDosage());
    medicalBill.setSpecialInstructions(prescribedDrug.getSpecialInstructions());
    // Drug names are now set in the createMedicalBill method
}

    private MedicalBillResponseDTO convertToResponseDTO(MedicalBill medicalBill) {
        MedicalBillResponseDTO responseDTO = new MedicalBillResponseDTO();
        responseDTO.setId(medicalBill.getId());
        responseDTO.setPatientName(medicalBill.getPatientName());
        responseDTO.setPatientDoB(medicalBill.getPatientDob());
        responseDTO.setPatientGender(medicalBill.getPatientGender());
        responseDTO.setSymptomName(medicalBill.getSymptomName());
        responseDTO.setSyndrome(medicalBill.getSyndrome());
        responseDTO.setDosage(medicalBill.getDosage());
        responseDTO.setSpecialInstructions(medicalBill.getSpecialInstructions());
        responseDTO.setDrugNames(medicalBill.getDrugNames()); // Set drug names here

        return responseDTO;
    }
}
