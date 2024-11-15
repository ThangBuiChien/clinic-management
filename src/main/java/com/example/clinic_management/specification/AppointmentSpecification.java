package com.example.clinic_management.specification;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.Predicate;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.domain.Specification;

import com.example.clinic_management.dtos.requests.AppointmentSearchCriteria;
import com.example.clinic_management.entities.Appointment;

@Configuration
public class AppointmentSpecification {

    public Specification<Appointment> getSearchSpecification(AppointmentSearchCriteria criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getId() != null) {
                predicates.add(cb.equal(root.get("id"), criteria.getId()));
            }

            if (criteria.getAppointmentDate() != null) {
                predicates.add(cb.equal(root.get("appointmentDate"), criteria.getAppointmentDate()));
            }

            if (criteria.getDoctorId() != null) {
                predicates.add(cb.equal(root.get("doctor").get("id"), criteria.getDoctorId()));
            }

            if (criteria.getPatientId() != null) {
                predicates.add(cb.equal(root.get("patient").get("id"), criteria.getPatientId()));
            }

            if (criteria.getAppointmentStatus() != null) {
                predicates.add(cb.equal(root.get("appointmentStatus"), criteria.getAppointmentStatus()));
            }

            if (criteria.getTimeSlot() != null) {
                predicates.add(cb.equal(root.get("timeSlot"), criteria.getTimeSlot()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
