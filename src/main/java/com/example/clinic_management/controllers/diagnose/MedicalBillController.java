package com.example.clinic_management.controllers.diagnose;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.clinic_management.dtos.requests.*;
import com.example.clinic_management.dtos.responses.MedicalBillResponseDTO;
import com.example.clinic_management.service.diagnose.MedicalBillService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/medical-bills")
@RequiredArgsConstructor
public class MedicalBillController {
    private final MedicalBillService medicalBillService;

    @PostMapping
    public ResponseEntity<MedicalBillResponseDTO> createMedicalBill(
            @Valid @RequestBody MedicalBillRequestDTO medicalBillRequestDTO) {
        MedicalBillResponseDTO responseDTO = medicalBillService.createMedicalBill(medicalBillRequestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @Transactional
    @PostMapping(value = "images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MedicalBillResponseDTO> createMedicalBillWithImages(
            @RequestPart("medicalBillData") MedicalBillRequestDTO medicalBillRequestDTO,
            @RequestPart("files") List<MultipartFile> files) {
        MedicalBillResponseDTO responseDTO =
                medicalBillService.createMedicalBillWithImage(medicalBillRequestDTO, files);

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("lab_request")
    public ResponseEntity<MedicalBillResponseDTO> createMedicalBillWithLabRequest(
            @Valid @RequestBody MedicalBillWithLabRequestDTO medicalBillWithLabRequestDTO) {
        MedicalBillResponseDTO responseDTO =
                medicalBillService.createMedicalBillWithLabRequireRequest(medicalBillWithLabRequestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PostMapping("pre_examination")
    public ResponseEntity<MedicalBillResponseDTO> createMedicalBillWithPreExamination(
            @Valid @RequestBody MedicalBillWithPreExaminationDTO medicalBillWithPreExaminationDTO) {
        MedicalBillResponseDTO responseDTO =
                medicalBillService.createMedicalBillWithPreExamination(medicalBillWithPreExaminationDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PostMapping("/{medicalBillId}/drugs")
    public ResponseEntity<MedicalBillResponseDTO> addDrugsToMedicalBill(
            @PathVariable Long medicalBillId,
            @Valid @RequestBody List<PrescribedDrugRequestDTO> prescribedDrugRequestDTOS) {
        MedicalBillResponseDTO responseDTO =
                medicalBillService.addDrugToMedicalBill(medicalBillId, prescribedDrugRequestDTOS);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/{medicalBillId}/lab_request")
    public ResponseEntity<MedicalBillResponseDTO> addLabRequestToMedicalBill(
            @PathVariable Long medicalBillId,
            @Valid @RequestBody List<ExaminationDetailLabRequestDTO> examinationDetailLabRequestDTOS) {
        MedicalBillResponseDTO responseDTO =
                medicalBillService.addLabRequestToMedicalBill(medicalBillId, examinationDetailLabRequestDTOS);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<Page<MedicalBillResponseDTO>> getMedicalBills(Pageable pageable) {
        Page<MedicalBillResponseDTO> responseDTOS = medicalBillService.getAllMedicalBills(pageable);
        return ResponseEntity.ok(responseDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalBillResponseDTO> getMedicalBillById(@PathVariable Long id) {
        MedicalBillResponseDTO responseDTO = medicalBillService.getMedicalBillById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/top/patient/{patientId}")
    public ResponseEntity<MedicalBillResponseDTO> getTopMedicalBillByPatientId(@PathVariable Long patientId) {
        MedicalBillResponseDTO responseDTO = medicalBillService.getTopMedicalBillByPatientId(patientId);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalBillResponseDTO> updateMedicalBill(
            @PathVariable Long id, @Valid @RequestBody MedicalBillRequestDTO medicalBillRequestDTO) {
        MedicalBillResponseDTO responseDTO = medicalBillService.updateMedicalBill(id, medicalBillRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalBill(@PathVariable Long id) {
        medicalBillService.deleteMedicalBill(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/patient/name/{patientName}")
    public ResponseEntity<List<MedicalBillResponseDTO>> getMedicalBillsByPatientName(@PathVariable String patientName) {
        List<MedicalBillResponseDTO> responseDTOS = medicalBillService.findMedicalBillsByPatientName(patientName);
        return ResponseEntity.ok(responseDTOS);
    }

    @GetMapping("/doctor/name/{doctorName}")
    public ResponseEntity<List<MedicalBillResponseDTO>> getMedicalBillsByDoctorName(@PathVariable String doctorName) {
        List<MedicalBillResponseDTO> responseDTOS = medicalBillService.findMedicalBillsByDoctorName(doctorName);
        return ResponseEntity.ok(responseDTOS);
    }

    @GetMapping("/patient/id/{patientId}")
    public ResponseEntity<Page<MedicalBillResponseDTO>> getMedicalBillsByPatientId(
            @PathVariable Long patientId, Pageable pageable) {
        Page<MedicalBillResponseDTO> responseDTOS = medicalBillService.findMedicalBillsByPatientId(patientId, pageable);
        return ResponseEntity.ok(responseDTOS);
    }

    @GetMapping("/doctor/id/{doctorId}")
    public ResponseEntity<Page<MedicalBillResponseDTO>> getMedicalBillsByDoctorId(
            @PathVariable Long doctorId, Pageable pageable) {
        Page<MedicalBillResponseDTO> responseDTOS = medicalBillService.findMedicalBillsByDoctorId(doctorId, pageable);
        return ResponseEntity.ok(responseDTOS);
    }
}
