package com.example.clinic_management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.clinic_management.dtos.requests.ExaminationDetailLabRequestDTO;
import com.example.clinic_management.dtos.requests.ExaminationDetailRequestDTO;
import com.example.clinic_management.dtos.responses.ExaminationDetailResponseDTO;
import com.example.clinic_management.entities.ExaminationDetail;

@Mapper(
        componentModel = "spring",
        uses = {MapperService.class, AutoImageMapper.class})
public interface AutoExaminationDetailMapper {

    ExaminationDetail toEntity(ExaminationDetailRequestDTO examinationDetailRequestDTO);

    ExaminationDetail fromExaminationDetailLabRequestDTOToEntity(
            ExaminationDetailLabRequestDTO examinationDetailLabRequestDTO);

    @Mapping(source = "imagesTest", target = "imageResponseDTO")
    @Mapping(source = "examinationType.department", target = "labDepartment")
    @Mapping(source = "examinationType.price", target = "labPrice")
    @Mapping(source = "status", target = "status")
    ExaminationDetailResponseDTO toResponse(ExaminationDetail examinationDetail);
}
 