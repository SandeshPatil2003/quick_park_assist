//package com.example.registration.controller;
//
//import com.example.registration.dto.VehicleDto;
//import com.example.registration.service.VehicleService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//
//@Controller
//public class VehicleController {
//
//    @Autowired
//    private  VehicleService vehicleService;
//
//    // Vehicle DTO Binding
//    @ModelAttribute("vehicle")
//    public VehicleDto vehicleDto() {
//        return new VehicleDto();
//    }
//
//
//    @GetMapping
//    public String showRegistrationForm(Model model) {
//        // Check if a user is logged in
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        boolean isLoggedIn = auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal());
//
//        // Pass the login status to the template
//        model.addAttribute("isLoggedIn", isLoggedIn);
//
//        return "registersection/registration";
//    }
//
//
//    // Vehicle Registration Submission
//    @PostMapping("/registration")
//    public String addVehicle(@ModelAttribute("vehicle") VehicleDto vehicleDto) {
//        vehicleService.save(vehicleDto);
//        return "redirect:/registration?vehicleSuccess";
//    }
//
//
//}
