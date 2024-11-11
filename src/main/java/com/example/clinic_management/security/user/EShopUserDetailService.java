package com.example.clinic_management.security.user;

import com.example.clinic_management.entities.Doctor;
import com.example.clinic_management.entities.Patient;
import com.example.clinic_management.entities.UserAbstractEntity;
import com.example.clinic_management.exception.ResourceNotFoundException;
import com.example.clinic_management.repository.DoctorRepository;
import com.example.clinic_management.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EShopUserDetailService implements UserDetailsService {

//    private final UserRepository userRepository;

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        UserAbstractEntity user =  userRepository.findByEmail(username)
//                .orElseThrow(() -> new ResourceNotFoundException("user not found with " + username));

        Doctor doctor = doctorRepository.findByEmail(username).orElse(null);
        if (doctor != null) {
            return EShopUserDetail.buildEShopUserDetail(doctor);
        }

        Patient patient = patientRepository.findByEmail(username).orElse(null);
        if (patient != null) {
            return EShopUserDetail.buildEShopUserDetail(patient);
        }

        throw new ResourceNotFoundException("User not found with email: " + username);
    }
}
