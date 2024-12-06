 # Clinic Management System

The **Clinic Management System** is an integrated platform designed to simplify and streamline healthcare clinic operations. It provides a user-friendly interface for both patients and healthcare providers, enhancing appointment scheduling, medical record management, and patient care. The system improves efficiency by automating routine tasks and facilitating seamless communication between patients and medical staff.

## Table of Contents
- [Project Overview](#project-overview)
- [Key Features](#key-features)
- [Technologies Used](#technologies-used)

## Project Overview
This system is built to address common challenges faced by healthcare clinics in managing appointments, doctor schedules, and medical records. It offers a variety of features designed to enhance the overall patient experience, while ensuring doctors and staff can manage their workflow efficiently.

Key highlights of the system include an **Appointment Booking System**, **Doctor Schedule Management**, **Medical Records Management**, **AI-Powered Support Chatbot**, and **Payment Integration**. The platform is designed to be scalable and secure, with strong emphasis on data privacy and efficient resource management.

## Key Features

- **Appointment Booking System**:  
  Patients can book, reschedule, and cancel appointments easily, while the system ensures no double-booking occurs through efficient scheduling. Doctors and staff can also manage appointment requests and track patient visits.
  
- **Doctor Schedule Management**:  
  Clinic administrators and doctors can view and update doctor schedules in real time, allowing for easy appointment coordination and better resource allocation.

- **Medical Records Management**:  
  Patient medical records are securely stored and easily retrievable, ensuring quick access by authorized medical staff. The system allows for recording detailed patient histories, diagnoses, and treatment plans.

- **AI-Powered Support Chatbot**:  
  A real-time, AI-driven chatbot provides assistance to patients, answering common queries, guiding users through the booking process, and offering general healthcare information.

- **Payment Integration**:  
  Patients can make payments for their medical services directly through the platform. The system integrates with a secure payment gateway (e.g., VNPAY) to handle billing and transactions.

## Technologies Used

- **Back-End**:  
  - Java Spring Boot (Spring Data JPA, Spring MVC, Spring Security)
  - MySQL for database management
  - Websocket, OPENAPI intergration, LangChain for real time AI chatbot
  - Redisson for distributed locking and handling race conditions
  - Docker for containerization

- **Payment Integration**:  
  - Stripe API for secure online payments

- **AI Integration**:  
  - OpenAI for powering the chatbot and natural language processing
