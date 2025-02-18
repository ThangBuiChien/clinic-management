package com.example.clinic_management.controllers.security;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.clinic_management.dtos.requests.LoginRequestDTO;
import com.example.clinic_management.dtos.responses.ApiResponse;
import com.example.clinic_management.dtos.responses.JwtResponseDTO;
import com.example.clinic_management.security.jwt.JwtUntils;
import com.example.clinic_management.security.user.EShopUserDetail;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUntils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequestDTO request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateTokenForUser(authentication);
            EShopUserDetail userDetails = (EShopUserDetail) authentication.getPrincipal();
            JwtResponseDTO jwtResponse = new JwtResponseDTO(userDetails.getId(), jwt);
            return ResponseEntity.ok(new ApiResponse(200, "Login Success!", jwtResponse));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(403, e.getMessage(), null));
        }
    }
}
