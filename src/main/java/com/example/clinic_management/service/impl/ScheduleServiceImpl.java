package com.example.clinic_management.service.impl;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.example.clinic_management.dtos.responses.ScheduleResponseDTO;
import com.example.clinic_management.mapper.AutoScheduleMapper;
import com.example.clinic_management.repository.ScheduleRepository;
import com.example.clinic_management.service.ScheduleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final AutoScheduleMapper autoScheduleMapper;

    @Override
    public ScheduleResponseDTO getSchedulesByDoctorIdAndDate(Long doctorId, LocalDate date) {
        return autoScheduleMapper.toResponseDTO(scheduleRepository.findByDoctorIdAndDate(doctorId, date));
    }
}
