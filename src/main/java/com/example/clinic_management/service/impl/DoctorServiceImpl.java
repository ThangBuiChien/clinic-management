package com.example.clinic_management.service.impl;

import com.example.clinic_management.specification.DoctorSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import com.example.clinic_management.service.user.DoctorService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final AutoDoctorMapper autoDoctorMapper;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;
    private final DoctorSpecifications doctorSpecifications;

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
        doctor.setRole(Role.ROLE_DOCTOR);
        doctor.setStatus(AccountStatus.ACTIVE);
        doctor.setPassword(passwordEncoder.encode(doctorRequestDTO.getPassword()));

        department.addDoctor(doctor);

        //        doctorRepository.save(doctor);

        return autoDoctorMapper.toResponseDTO(doctorRepository.save(doctor));
    }

    @Override
    public DoctorResponseDTO addNurse(DoctorRequestDTO doctorRequestDTO) {
        Department department = departmentRepository
                .findById(doctorRequestDTO.getDepartmentId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Department", "id", doctorRequestDTO.getDepartmentId()));

        Doctor doctor = autoDoctorMapper.toEntity(doctorRequestDTO);
        doctor.setRole(Role.ROLE_NURSE);
        doctor.setStatus(AccountStatus.ACTIVE);
        doctor.setPassword(passwordEncoder.encode(doctorRequestDTO.getPassword()));

        department.addDoctor(doctor);

        //        doctorRepository.save(doctor);

        return autoDoctorMapper.toResponseDTO(doctorRepository.save(doctor));
    }

    @Override
    public DoctorResponseDTO updateDoctor(Long id, DoctorRequestDTO doctorRequestDTO) {
        Doctor doctor =
                doctorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id));

        autoDoctorMapper.updateFromDTO(doctorRequestDTO, doctor);

        if (doctorRequestDTO.getPassword() != null) {
            doctor.setPassword(passwordEncoder.encode(doctorRequestDTO.getPassword()));
        }

        return autoDoctorMapper.toResponseDTO(doctorRepository.save(doctor));
    }

    @Override
    public DoctorResponseDTO updatePartialDoctor(Long id, DoctorPartialUpdateDTO doctorPartialUpdateDTO) {
        Doctor doctor =
                doctorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id));

        autoDoctorMapper.updatePartialFromDTO(doctorPartialUpdateDTO, doctor);

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

    @Override
    public Page<DoctorResponseDTO> getDoctorsByNameAndDepartment(String name, Long departmentId,
                                                                 Pageable pageable) {
        return doctorRepository.findAll(doctorSpecifications.filterByNameAndDepartment(name, departmentId), pageable)
                .map(autoDoctorMapper::toResponseDTO);
    }
}
