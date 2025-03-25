package com.example.clinic_management.controllers.security;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.clinic_management.security.service.AuthenticationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/authz")
public class AuthorizationTestingController {

    private final AuthenticationService authenticationService;

    @GetMapping("/public")
    public String publicEndpoint() {
        return "This is a public endpoint.";
    }

    @GetMapping("/login")
    public String loginEndpoint() {
        return "This is a login endpoint.";
    }

    @GetMapping("/patient")
    public String patientEndpoint() {
        return "This endpoint is accessible by users with the PATIENT role.";
    }

    @GetMapping("/doctor")
    public String doctorEndpoint() {
        return "This endpoint is accessible by users with the DOCTOR role.";
    }

    @GetMapping("/getIdFromJWT")
    public String getIdFromJWT() {

        Long userId = authenticationService.getAuthenticatedUserId();
        return "Your user id is " + userId;
    }
}
