package com.example.registration.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.registration.dto.UserRegistrationDto;
import com.example.registration.dto.VehicleDto;
import com.example.registration.service.UserService;
import com.example.registration.service.VehicleService;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {

	private final UserService userService;
	private final VehicleService vehicleService;

	public UserRegistrationController(UserService userService, VehicleService vehicleService) {
		this.userService = userService;
		this.vehicleService = vehicleService;
	}

	// User Registration DTO Binding
	@ModelAttribute("user")
	public UserRegistrationDto userRegistrationDto() {
		return new UserRegistrationDto();
	}

	// Vehicle DTO Binding
	@ModelAttribute("vehicle")
	public VehicleDto vehicleDto() {
		return new VehicleDto();
	}

	@GetMapping
	public String showRegistrationForm(Model model) {
		// Check if a user is logged in
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		boolean isLoggedIn = auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal());

		// Pass the login status to the template
		model.addAttribute("isLoggedIn", isLoggedIn);

		return "registersection/registration";
	}

	// User Registration Submission
	@PostMapping
	public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto registrationDto) {
		userService.save(registrationDto);
		return "redirect:/registration?success";
	}

//	@PostMapping("/add-vehicle")
//    public String addVehicle(@ModelAttribute("vehicle") VehicleDto vehicleDto) {
//        vehicleService.save(vehicleDto);
//        return "redirect:/registration?vehicleSuccess";
//    }
////
}
