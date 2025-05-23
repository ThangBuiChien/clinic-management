package com.example.clinic_management.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.clinic_management.entities.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long>, JpaSpecificationExecutor<Appointment> {
    List<Appointment> findByDoctorIdAndAppointmentDate(Long doctorId, LocalDate date);

    List<Appointment> findByDoctorIdAndAppointmentDate(Long doctorId, LocalDate date, Sort sort);

    Optional<Appointment> findByPayId(Long payId);

    Page<Appointment> findByPatientId(Long patientId, Pageable pageable);

    Optional<Appointment> findTopByPatientIdOrderByIdDesc(@Param("patientId") Long patientId);

    List<Appointment> findByAppointmentDate(LocalDate date);
}
