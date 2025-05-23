package com.example.clinic_management.service.impl;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.clinic_management.dtos.requests.ExaminationDetailUploadImgRequestDTO;
import com.example.clinic_management.dtos.requests.ExaminationRequestDTO;
import com.example.clinic_management.dtos.responses.ExaminationDetailResponseDTO;
import com.example.clinic_management.entities.*;
import com.example.clinic_management.enums.AppointmentStatus;
import com.example.clinic_management.enums.LabDepartment;
import com.example.clinic_management.enums.LabTest;
import com.example.clinic_management.mapper.AutoExaminationDetailMapper;
import com.example.clinic_management.repository.AppointmentRepository;
import com.example.clinic_management.repository.ExaminationRepository;
import com.example.clinic_management.repository.PatientRepository;
import com.example.clinic_management.service.booking.AppointmentService;
import com.example.clinic_management.service.diagnose.ExaminationDetailService;
import com.example.clinic_management.service.diagnose.ImageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExaminationDetailServiceImpl implements ExaminationDetailService {

    private final ExaminationRepository examinationRepository;
    private final PatientRepository patientRepository;
    private final ImageService imageService;
    private final AutoExaminationDetailMapper autoExaminationDetailMapper;
    private final AppointmentService appointmentService;
    private final AppointmentRepository appointmentRepository;

    @Override
    @Transactional
    public ExaminationDetail createExaminationDetail(ExaminationRequestDTO examinationRequestDTO) {

        Patient patient = validateAndGetExaminationDetail(examinationRequestDTO);
        ExaminationDetail examinationDetail = new ExaminationDetail();
        //        examinationDetail.setPatient(patient);
        examinationDetail.setPatientName(patient.getFullName());
        //        examinationDetail.setExaminationType(
        //                examinationRequestDTO.getExaminationType().trim());
        examinationDetail.setExaminationResult(
                examinationRequestDTO.getExaminationResult().trim());
        return examinationRepository.save(examinationDetail);
    }

    @Override
    @Transactional
    public ExaminationDetailResponseDTO getExaminationDetailById(Long id) {
        return examinationRepository
                .findById(id)
                .map(autoExaminationDetailMapper::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Examination not found with" + id));
    }

    @Override
    @Transactional
    public List<ExaminationDetail> getAllExaminationDetails() {
        return examinationRepository.findAll();
    }

    @Transactional
    @Override
    public List<ExaminationDetailResponseDTO> updateExaminationDetailWithImages(
            List<ExaminationDetailUploadImgRequestDTO> examinationDetailUploadImgRequestDTOs,
            List<MultipartFile> files) {

        List<Image> images =
                files.stream().map(imageService::convertMultipartFileToImage).toList();

        List<ExaminationDetail> updatedExaminationDetail = new ArrayList<>();

        int fileIndex = 0;

        for (ExaminationDetailUploadImgRequestDTO dto : examinationDetailUploadImgRequestDTOs) {
            ExaminationDetail examinationDetail = examinationRepository
                    .findById(dto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Examination not found with id: " + dto.getId()));

            //            examinationDetail.setExaminationType(dto.getExaminationType());
            examinationDetail.setExaminationResult(dto.getExaminationResult());

            List<Image> tempImages = new ArrayList<>();
            for (int i = 0; i < dto.getImagesCount() && fileIndex < files.size(); i++, fileIndex++) {
                tempImages.add(images.get(fileIndex));
            }

            //            examinationDetail.setImagesTest(tempImages);
            examinationDetail.addImage(tempImages);

            updatedExaminationDetail.add(examinationDetail);
            examinationRepository.save(examinationDetail);
        }

        for (ExaminationDetail detail : updatedExaminationDetail) {
            for (Image image : detail.getImagesTest()) {
                imageService.updateUrlDownload(image);
            }
        }

        if (!updatedExaminationDetail.isEmpty()) {
            checkMedicalBillDoneAllExaminationDetail(
                    updatedExaminationDetail.get(0).getMedicalBill().getExaminationDetails());
        }

        return updatedExaminationDetail.stream()
                .map(autoExaminationDetailMapper::toResponse)
                .toList();
    }

    @Override
    public Set<LabTest> getLabTestsByDepartment(LabDepartment labDepartment) {
        return LabTest.getTestsByDepartment(labDepartment);
    }

    @Override
    public Page<ExaminationDetailResponseDTO> getExaminationDetailsByExaminationTypeAndImagesTestIsEmpty(
            LabTest examinationType, Pageable pageable) {
        return examinationRepository
                .findByExaminationTypeAndImagesTestIsEmpty(examinationType, pageable)
                .map(autoExaminationDetailMapper::toResponse);
    }

    @Override
    public Page<ExaminationDetailResponseDTO> getExaminationDetailsByDepartmentAndImagesTestIsEmpty(
            LabDepartment labDepartment, Pageable pageable) {
        Collection<LabTest> testsInDepartment = Arrays.stream(LabTest.values())
                .filter(test -> test.getDepartment() == labDepartment)
                .collect(Collectors.toList());

        return examinationRepository
                .findByDepartmentAndImagesTestIsEmpty(testsInDepartment, pageable)
                .map(autoExaminationDetailMapper::toResponse);
    }

    @Override
    public Page<ExaminationDetailResponseDTO> getAllExaminationDetailsByImagesTestIsEmpty(Pageable pageable) {
        Collection<LabTest> allTests = Arrays.stream(LabTest.values()).toList();

        return examinationRepository
                .findByDepartmentAndImagesTestIsEmpty(allTests, pageable)
                .map(autoExaminationDetailMapper::toResponse);
    }

    @Override
    public Page<ExaminationDetailResponseDTO> getExaminationDetailsByDepartmentAndImagesTestIsEmptyAndCreatedAt(LabDepartment labDepartment, LocalDate createdAt, Pageable pageable) {
        Collection<LabTest> testsInDepartment = Arrays.stream(LabTest.values())
                .filter(test -> test.getDepartment() == labDepartment)
                .collect(Collectors.toList());

        return examinationRepository
                .findByDepartmentAndImagesTestIsEmptyAndCreatedAt(testsInDepartment, createdAt, pageable)
                .map(autoExaminationDetailMapper::toResponse);
    }

    @Override
    public Page<ExaminationDetailResponseDTO> getExaminationDetailsByExaminationTypeAndImagesTestIsEmptyAndCreatedAt(LabTest examinationType, LocalDate createdAt, Pageable pageable) {
        return examinationRepository
                .findExaminationTypeAndImagesTestIsEmptyAndCreatedAt(examinationType, createdAt, pageable)
                .map(autoExaminationDetailMapper::toResponse);
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

    private void checkMedicalBillDoneAllExaminationDetail(List<ExaminationDetail> examinationDetails) {

        boolean isAllDone = examinationDetails.stream().allMatch(ExaminationDetail::isDone);
        if (isAllDone) {
            MedicalBill medicalBill = examinationDetails.get(0).getMedicalBill();
            Optional<Appointment> appointment = appointmentRepository.findTopByPatientIdOrderByIdDesc(
                    medicalBill.getPatient().getId());
            appointment.ifPresent(value ->
                    appointmentService.updateAppointmentStatus(value.getId(), AppointmentStatus.LAB_TEST_COMPLETED));
        }
    }
}
