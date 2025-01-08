package com.example.registration.repository;


import com.example.registration.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TokenRepository extends JpaRepository<PasswordResetToken, Integer>{

    PasswordResetToken findByToken(String token);

}
