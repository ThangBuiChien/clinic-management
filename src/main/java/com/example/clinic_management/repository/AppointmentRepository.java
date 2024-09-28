package com.example.clinic_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.clinic_management.entities.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {}
