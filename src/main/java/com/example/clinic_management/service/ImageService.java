package com.example.clinic_management.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.clinic_management.dtos.responses.ImageResponseDTO;
import com.example.clinic_management.entities.Image;

public interface ImageService {

    Image getImageById(Long id);

    void deleteImageById(Long id);

    List<ImageResponseDTO> saveImages(Long productId, List<MultipartFile> files);

    void updateImage(MultipartFile file, Long imageId);

    Image convertMultipartFileToImage(MultipartFile file);

    void updateUrlDownload(Image image);
}
