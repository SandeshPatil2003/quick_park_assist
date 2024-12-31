package com.example.registration.service;


import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.registration.dto.UserRegistrationDto;
import com.example.registration.model.User;

public interface UserService extends UserDetailsService {
    // Save a new user
    User save(UserRegistrationDto registrationDto);


    //boolean deleteUserByEmail(String email);    
    //void deleteUserByEmail(String email);
    

}
