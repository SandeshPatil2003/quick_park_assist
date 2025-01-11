package com.example.registration.service;


import com.example.registration.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.registration.dto.UserRegistrationDto;
import com.example.registration.model.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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
    

}
