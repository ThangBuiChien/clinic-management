package com.example.clinic_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.clinic_management.entities.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {}
