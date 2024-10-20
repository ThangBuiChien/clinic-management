package com.example.clinic_management.mapper;

import com.example.clinic_management.dtos.requests.ExaminationDetailRequestDTO;
import com.example.clinic_management.dtos.responses.ExaminationDetailResponseDTO;
import com.example.clinic_management.entities.ExaminationDetail;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        uses = {MapperService.class})
public interface AutoExaminationDetailMapper {

    ExaminationDetail toEntity(ExaminationDetailRequestDTO examinationDetailRequestDTO);

    ExaminationDetailResponseDTO toResponse(ExaminationDetail examinationDetail);
}
