package com.example.clinic_management.specification;

import com.example.clinic_management.dtos.requests.AppointmentSearchCriteria;
import com.example.clinic_management.entities.Doctor;
import jakarta.persistence.criteria.Predicate;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class DoctorSpecifications {

    public Specification<Doctor> filterByNameAndDepartment(String name, Long departmentId) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("fullName")), "%" + name.toLowerCase() + "%"));
            }
            if (departmentId != null) {
                predicates.add(cb.equal(root.get("department").get("id"), departmentId));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
