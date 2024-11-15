package com.example.clinic_management.repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.clinic_management.entities.Doctor;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Page<Doctor> findAllByDepartmentId(Long departmentId, Pageable pageable);

    List<Doctor> findAllByDepartmentIdAndWorkingDaysIn(Long departmentId, Set<DayOfWeek> workingDays);

    Optional<Doctor> findByEmail(String email);
}
