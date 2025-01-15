package com.example.quickParkAssist.service;


import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.quickParkAssist.dto.UserRegistrationDto;
import com.example.quickParkAssist.model.User;

public interface UserService extends UserDetailsService {
    // Save a new user
    User save(UserRegistrationDto registrationDto);

    // Find a user's ID by email (username is email)
    Long findUserIdByUsername(String email);  // Change parameter name to email
    // Method declaration to fetch mobile number by user ID
    String findMobileNumberByUserId(Long userId);

    User findByUsername(String username);
    User findUserById(Long id);
    //boolean deleteUserByEmail(String email);
    //void deleteUserByEmail(String email);
    boolean emailExists(String email);



}
