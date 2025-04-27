package com.example.clinic_management.specification;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.Predicate;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.domain.Specification;

import com.example.clinic_management.entities.Doctor;

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
