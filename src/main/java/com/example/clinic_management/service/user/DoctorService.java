package com.example.clinic_management.service.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.clinic_management.dtos.requests.DoctorPartialUpdateDTO;
import com.example.clinic_management.dtos.requests.DoctorRequestDTO;
import com.example.clinic_management.dtos.responses.DoctorResponseDTO;

public interface DoctorService {

    Page<DoctorResponseDTO> getAllDoctors(Pageable pageable);

    DoctorResponseDTO getDoctorById(Long id);

    Page<DoctorResponseDTO> getDoctorsByDepartmentId(Long departmentId, Pageable pageable);

    DoctorResponseDTO addDoctor(DoctorRequestDTO doctorRequestDTO);

    DoctorResponseDTO addNurse(DoctorRequestDTO doctorRequestDTO);

    DoctorResponseDTO updateDoctor(Long id, DoctorRequestDTO doctorRequestDTO);

    DoctorResponseDTO updatePartialDoctor(Long id, DoctorPartialUpdateDTO doctorPartialUpdateDTO);

    void deleteDoctor(Long id);

    Page<DoctorResponseDTO> getDoctorsByNameAndDepartment(String name, Long departmentId, Pageable pageable);
}
