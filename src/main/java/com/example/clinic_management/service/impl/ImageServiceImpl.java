package com.example.clinic_management.service.impl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.rowset.serial.SerialBlob;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.clinic_management.dtos.responses.ImageResponseDTO;
import com.example.clinic_management.entities.ExaminationDetail;
import com.example.clinic_management.entities.Image;
import com.example.clinic_management.exception.ResourceNotFoundException;
import com.example.clinic_management.mapper.AutoMapperPicture;
import com.example.clinic_management.repository.ExaminationRepository;
import com.example.clinic_management.repository.ImageRepository;
import com.example.clinic_management.service.diagnose.ImageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final ExaminationRepository examinationRepository;
    private final AutoMapperPicture autoMapperPicture;

    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("image", "id", id));
    }

    @Override
    public void deleteImageById(Long id) {
        imageRepository.findById(id).ifPresentOrElse(imageRepository::delete, () -> {
            throw new ResourceNotFoundException("image", "id", id);
        });
    }

    @Override
    public List<ImageResponseDTO> saveImages(Long examinationId, List<MultipartFile> files) {
        ExaminationDetail product = examinationRepository
                .findById(examinationId)
                .orElseThrow(() -> new ResourceNotFoundException("exmination", "id", examinationId));
        List<ImageResponseDTO> savedImages = new ArrayList<>();

        for (MultipartFile file : files) {
            Image savedImage = processAndSaveImage(file, product);
            savedImages.add(autoMapperPicture.toResponse(savedImage));
        }

        return savedImages;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        Image image = imageRepository
                .findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("image", "id", imageId));
        try {
            updateImageDetails(file, image);
            imageRepository.save(image);
        } catch (IOException | SQLException e) {
            throw new RuntimeException("Failed to update image: " + e.getMessage(), e);
        }
    }

    @Override
    public Image convertMultipartFileToImage(MultipartFile file) {
        try {
            Image image = new Image();
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            image.setDownloadUrl("temp"); // temp for by pass constraint @NotBlank

            return image;
        } catch (IOException | SQLException e) {
            throw new RuntimeException("Failed to save image: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateUrlDownload(Image image) {
        image.setDownloadUrl("/api/images/download/" + image.getId());
        imageRepository.save(image);
    }

    private Image processAndSaveImage(MultipartFile file, ExaminationDetail examinationDetail) {
        try {
            Image image = new Image();
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            image.setExaminationDetail(examinationDetail);
            image.setDownloadUrl("temp"); // temp for by pass constraint @NotBlank

            Image savedImage = imageRepository.save(image);
            String downloadUrl = "/api/v1/images/image/download/" + savedImage.getId();
            savedImage.setDownloadUrl(downloadUrl);

            return imageRepository.save(savedImage);
        } catch (IOException | SQLException e) {
            throw new RuntimeException("Failed to save image: " + e.getMessage(), e);
        }
    }

    private void updateImageDetails(MultipartFile file, Image image) throws IOException, SQLException {
        image.setFileName(file.getOriginalFilename());
        image.setFileType(file.getContentType());
        image.setImage(new SerialBlob(file.getBytes()));
    }
}
