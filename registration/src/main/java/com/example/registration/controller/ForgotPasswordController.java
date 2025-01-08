package com.example.registration.controller;

import com.example.registration.dto.UserRegistrationDto;
import com.example.registration.model.PasswordResetToken;
import com.example.registration.model.User;
import com.example.registration.repository.TokenRepository;
import com.example.registration.repository.UserRepository;
import com.example.registration.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;
import org.springframework.ui.Model;
import org.springframework.security.crypto.password.PasswordEncoder;
@Controller
public class ForgotPasswordController {



    @Autowired
    PasswordResetService userDetailsService;


    @Autowired
    UserRepository userRepository;
    @Autowired
    TokenRepository tokenRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    Random random = new Random(1000);

    // email id form open handler

    @GetMapping("/forgotPassword")
    public String forgotPassword() {
        return "forgotPassword";
    }

    @PostMapping("/forgotPassword")
    public String forgotPassordProcess(@ModelAttribute UserRegistrationDto userDTO) {
        String output = "";
        User user = userRepository.findByEmail(userDTO.getEmail());
        if (user != null) {
            output = userDetailsService.sendEmail(user);
        }
        if (output.equals("success")) {
            return "redirect:/forgotPassword?success";
        }

        return "redirect:/login?error";
    }
    @GetMapping("/resetPassword/{token}")
    public String resetPasswordForm(@PathVariable String token, Model model) {
        PasswordResetToken reset = tokenRepository.findByToken(token);
        if (reset != null && userDetailsService.hasExipred(reset.getExpiryDateTime())) {
            model.addAttribute("email", reset.getUser().getEmail());
            return "resetPassword";
        }
        return "redirect:/forgotPassword?error";
    }

    @PostMapping("/resetPassword")
    public String passwordResetProcess(@ModelAttribute UserRegistrationDto userDTO) {
        User user = userRepository.findByEmail(userDTO.getEmail());
        if(user != null) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            userRepository.save(user);
        }
        return "redirect:/login";
    }


}
