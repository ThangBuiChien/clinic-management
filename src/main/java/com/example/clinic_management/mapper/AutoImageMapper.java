package com.example.clinic_management.mapper;

import org.mapstruct.Mapper;

import com.example.clinic_management.dtos.responses.ImageResponseDTO;
import com.example.clinic_management.entities.Image;

@Mapper(componentModel = "spring")
public interface AutoImageMapper {

    ImageResponseDTO toResponse(Image image);
}
