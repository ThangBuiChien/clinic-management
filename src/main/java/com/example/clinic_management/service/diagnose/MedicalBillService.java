package com.example.clinic_management.service.diagnose;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.example.clinic_management.dtos.requests.*;
import com.example.clinic_management.dtos.responses.MedicalBillResponseDTO;

public interface MedicalBillService {
    MedicalBillResponseDTO createMedicalBill(MedicalBillRequestDTO medicalBillRequestDTO);

    MedicalBillResponseDTO createMedicalBillWithImage(
            MedicalBillRequestDTO medicalBillRequestDTO, List<MultipartFile> files);

    MedicalBillResponseDTO createMedicalBillWithLabRequireRequest(
            MedicalBillWithLabRequestDTO medicalBillWithLabRequestDTO);

    MedicalBillResponseDTO createMedicalBillWithPreExamination(
            MedicalBillWithPreExaminationDTO medicalBillWithPreExaminationDTO);

    MedicalBillResponseDTO addDrugToMedicalBill(
            Long medicalBillId, List<PrescribedDrugRequestDTO> prescribedDrugRequestDTOS);

    MedicalBillResponseDTO addLabRequestToMedicalBill(
            Long medicalBillId, List<ExaminationDetailLabRequestDTO> examinationDetailLabRequestDTOS);

    MedicalBillResponseDTO getMedicalBillById(Long id);

    List<MedicalBillResponseDTO> getAllMedicalBills();

    Page<MedicalBillResponseDTO> getAllMedicalBills(Pageable pageable);

    List<MedicalBillResponseDTO> findMedicalBillsByPatientName(String patientName);

    List<MedicalBillResponseDTO> findMedicalBillsByDoctorName(String doctorName);

    Page<MedicalBillResponseDTO> findMedicalBillsByPatientId(Long patientId, Pageable pageable);

    Page<MedicalBillResponseDTO> findMedicalBillsByDoctorId(Long doctorId, Pageable pageable);

    MedicalBillResponseDTO updateMedicalBill(Long id, MedicalBillRequestDTO medicalBillRequestDTO);

    void deleteMedicalBill(Long id);

    MedicalBillResponseDTO getTopMedicalBillByPatientId(Long patientId);

    MedicalBillResponseDTO partialUpdateMedicalBill(Long id, MedicalBillPartialUpdateRequestDTO dto);

    public MedicalBillResponseDTO updateExaminationDetailsStatusAndCalculateTotal(Long medicalBillId);
}
