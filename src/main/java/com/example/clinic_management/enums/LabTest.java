package com.example.clinic_management.enums;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public enum LabTest {
    WHITE_BLOOD_CELL_COUNT(LabDepartment.HEMATOLOGY, 50.0),
    RED_BLOOD_CELL_COUNT(LabDepartment.HEMATOLOGY, 45.0),
    PLATELET_COUNT(LabDepartment.HEMATOLOGY, 40.0),
    HEMOGLOBIN(LabDepartment.HEMATOLOGY, 35.0),
    LIVER_FUNCTION(LabDepartment.BIOCHEMISTRY, 100.0),
    BLOOD_GLUCOSE(LabDepartment.BIOCHEMISTRY, 80.0),
    LIPID_PROFILE(LabDepartment.BIOCHEMISTRY, 120.0),
    KIDNEY_FUNCTION(LabDepartment.BIOCHEMISTRY, 90.0),
    X_RAY(LabDepartment.RADIOLOGY, 150.0),
    MRI(LabDepartment.RADIOLOGY, 500.0),
    CT_SCAN(LabDepartment.RADIOLOGY, 400.0),
    ULTRASOUND(LabDepartment.RADIOLOGY, 200.0);

    private final LabDepartment department;
    private final double price;

    LabTest(LabDepartment department, double price) {
        this.department = department;
        this.price = price;
    }

    public LabDepartment getDepartment() {
        return department;
    }

    public double getPrice() {
        return price;
    }

    public static Set<LabTest> getTestsByDepartment(LabDepartment department) {
        Set<LabTest> tests = EnumSet.noneOf(LabTest.class);
        for (LabTest test : LabTest.values()) {
            if (test.getDepartment() == department) {
                tests.add(test);
            }
        }
        return tests;
    }

    public static Page<LabTest> getTestsByDepartment(LabDepartment department, Pageable pageable) {
        List<LabTest> tests = EnumSet.allOf(LabTest.class).stream()
                .filter(test -> test.getDepartment() == department)
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), tests.size());
        List<LabTest> pagedTests = tests.subList(start, end);

        return new PageImpl<>(pagedTests, pageable, tests.size());
    }
}
