package com.example.clinic_management.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.clinic_management.dtos.requests.DoctorPartialUpdateDTO;
import com.example.clinic_management.dtos.requests.DoctorRequestDTO;
import com.example.clinic_management.dtos.responses.DoctorResponseDTO;
import com.example.clinic_management.entities.Department;
import com.example.clinic_management.entities.Doctor;
import com.example.clinic_management.enums.AccountStatus;
import com.example.clinic_management.enums.Role;
import com.example.clinic_management.exception.ResourceNotFoundException;
import com.example.clinic_management.mapper.AutoDoctorMapper;
import com.example.clinic_management.repository.DepartmentRepository;
import com.example.clinic_management.repository.DoctorRepository;
import com.example.clinic_management.service.DoctorService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final AutoDoctorMapper autoDoctorMapper;
    private final DepartmentRepository departmentRepository;

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
    @Transactional
    public DoctorResponseDTO addDoctor(DoctorRequestDTO doctorRequestDTO) {
        Department department = departmentRepository
                .findById(doctorRequestDTO.getDepartmentId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Department", "id", doctorRequestDTO.getDepartmentId()));

        Doctor doctor = autoDoctorMapper.toEntity(doctorRequestDTO);
        doctor.setRole(Role.DOCTOR);
        doctor.setStatus(AccountStatus.ACTIVE);

        department.addDoctor(doctor);

        //        doctorRepository.save(doctor);

        return autoDoctorMapper.toResponseDTO(doctorRepository.save(doctor));
    }

    @Override
    public DoctorResponseDTO updateDoctor(Long id, DoctorRequestDTO doctorRequestDTO) {
        Doctor oldDoctor =
                doctorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id));
        Doctor newDoctor = autoDoctorMapper.toEntity(doctorRequestDTO);
        newDoctor.setId(oldDoctor.getId());
        newDoctor.getDepartment().addDoctor(newDoctor);
        return autoDoctorMapper.toResponseDTO(doctorRepository.save(newDoctor));
    }

    @Override
    public DoctorResponseDTO updatePartialDoctor(Long id, DoctorPartialUpdateDTO doctorPartialUpdateDTO) {
        Doctor doctor =
                doctorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id));

        autoDoctorMapper.updateFromDTO(doctorPartialUpdateDTO, doctor);

        return autoDoctorMapper.toResponseDTO(doctorRepository.save(doctor));
    }

    @Override
    public void deleteDoctor(Long id) {
        Doctor doctor =
                doctorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id));
        if (doctor.getDepartment() != null) {
            doctor.getDepartment().getDoctors().remove(doctor);
        }
        doctorRepository.deleteById(id);
    }
}
