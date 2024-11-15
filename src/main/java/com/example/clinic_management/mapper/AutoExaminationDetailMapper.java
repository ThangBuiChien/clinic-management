package com.example.clinic_management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.clinic_management.dtos.requests.ExaminationDetailRequestDTO;
import com.example.clinic_management.dtos.responses.ExaminationDetailResponseDTO;
import com.example.clinic_management.entities.ExaminationDetail;

@Mapper(
        componentModel = "spring",
        uses = {MapperService.class, AutoImageMapper.class})
public interface AutoExaminationDetailMapper {

    ExaminationDetail toEntity(ExaminationDetailRequestDTO examinationDetailRequestDTO);

    @Mapping(source = "imagesTest", target = "imageResponseDTO")
    ExaminationDetailResponseDTO toResponse(ExaminationDetail examinationDetail);
}
