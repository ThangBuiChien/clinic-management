package com.example.clinic_management.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.example.clinic_management.dtos.requests.ExaminationDetailUploadImgRequestDTO;
import com.example.clinic_management.dtos.responses.ExaminationDetailResponseDTO;
import com.example.clinic_management.entities.Image;
import com.example.clinic_management.mapper.AutoExaminationDetailMapper;
import com.example.clinic_management.service.ImageService;
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
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ExaminationDetailServiceImpl implements ExaminationDetailService {

    private final ExaminationRepository examinationRepository;
    private final PatientRepository patientRepository;
    private final ImageService imageService;
    private final AutoExaminationDetailMapper autoExaminationDetailMapper;

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



        return updatedExaminationDetail.stream()
                .map(autoExaminationDetailMapper::toResponse)
                .toList();
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
