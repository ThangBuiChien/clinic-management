package com.example.clinic_management.repository;

import com.example.clinic_management.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
