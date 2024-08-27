package com.example.clinic_management.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.clinic_management.dtos.requests.DoctorRequestDTO;
import com.example.clinic_management.dtos.responses.DoctorResponseDTO;
import com.example.clinic_management.entities.Doctor;
import com.example.clinic_management.enums.AccountStatus;
import com.example.clinic_management.enums.Role;
import com.example.clinic_management.exception.ResourceNotFoundException;
import com.example.clinic_management.mapper.AutoDoctorMapper;
import com.example.clinic_management.repository.DoctorRepository;
import com.example.clinic_management.service.DoctorService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final AutoDoctorMapper autoDoctorMapper;

    @Override
    public Page<DoctorResponseDTO> getAllDoctors(Pageable pageable) {
        return doctorRepository.findAll(pageable).map(autoDoctorMapper::toResponseDTO);
    }

    @Override
    public DoctorResponseDTO getDoctorById(Long id) {
        return doctorRepository
                .findById(id)
                .map(autoDoctorMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id));
    }

    @Override
    public Page<DoctorResponseDTO> getDoctorsByDepartmentId(Long departmentId, Pageable pageable) {
        return doctorRepository.findAllByDepartmentId(departmentId, pageable).map(autoDoctorMapper::toResponseDTO);
    }

    @Override
    public DoctorResponseDTO addDoctor(DoctorRequestDTO doctorRequestDTO) {
        Doctor doctor = autoDoctorMapper.toEntity(doctorRequestDTO);
        doctor.setRole(Role.DOCTOR);
        doctor.setStatus(AccountStatus.ACTIVE);

        doctorRepository.save(doctor);

        return autoDoctorMapper.toResponseDTO(doctorRepository.save(doctor));
    }

    @Override
    public DoctorResponseDTO updateDoctor(Long id, DoctorRequestDTO doctorRequestDTO) {
        Doctor oldDoctor =
                doctorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id));
        Doctor newDoctor = autoDoctorMapper.toEntity(doctorRequestDTO);
        newDoctor.setId(oldDoctor.getId());
        return autoDoctorMapper.toResponseDTO(doctorRepository.save(newDoctor));
    }

    @Override
    public void deleteDoctor(Long id) {
        doctorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id));
        doctorRepository.deleteById(id);
    }
}
