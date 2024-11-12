package com.example.clinic_management.mapper;

import com.example.clinic_management.dtos.responses.ImageResponseDTO;
import com.example.clinic_management.entities.Image;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AutoMapperPicture {

    ImageResponseDTO toResponse(Image image);

}
