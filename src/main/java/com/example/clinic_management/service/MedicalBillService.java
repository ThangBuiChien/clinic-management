package com.example.clinic_management.service;

import com.example.clinic_management.dtos.requests.MedicalBillRequestDTO;
import com.example.clinic_management.dtos.responses.MedicalBillResponseDTO;

public interface MedicalBillService {
    MedicalBillResponseDTO createMedicalBill(MedicalBillRequestDTO medicalBillRequestDTO);
}
