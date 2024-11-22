package com.example.clinic_management.security.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.clinic_management.security.user.EShopUserDetail;

@Service
public class AuthenticationService {

    public Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found");
        }
        EShopUserDetail userPrincipal = (EShopUserDetail) authentication.getPrincipal();
        return userPrincipal.getId();
    }
}
