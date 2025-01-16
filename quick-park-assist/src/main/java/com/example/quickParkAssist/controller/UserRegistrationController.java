package com.example.quickParkAssist.controller;

import com.example.quickParkAssist.model.User;
import com.example.quickParkAssist.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.quickParkAssist.dto.UserRegistrationDto;
import com.example.quickParkAssist.dto.VehicleDto;
import com.example.quickParkAssist.service.UserService;
import com.example.quickParkAssist.service.VehicleService;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {


	private final UserService userService;


	@Autowired
	private UserServiceImpl userServiceImpl;

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

//	@GetMapping
//	public String showRegistrationForm(Model model, @AuthenticationPrincipal UserDetails loggedInUser) {
//
//		// Check if a user is logged in
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		boolean isLoggedIn = auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal());
//
//		// Pass the login status to the template
//		model.addAttribute("isLoggedIn", isLoggedIn);
//		// Fetch logged-in user details if logged in
//		if (loggedInUser != null) {
//			// Fetch user by email (assuming username is email)
//			User user = userServiceImpl.getUserByEmail(loggedInUser.getUsername());
//			model.addAttribute("user", user); // Pass user object to the view
//		} else {
//			model.addAttribute("user", null); // No user is logged in
//		}
//
//		return "registersection/registration";
//	}
@GetMapping
public String showRegistrationForm(Model model, @AuthenticationPrincipal UserDetails loggedInUser) {

	// Check if a user is logged in
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	boolean isLoggedIn = auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal());

	// Pass the login status
	model.addAttribute("isLoggedIn", isLoggedIn);

	if (isLoggedIn && loggedInUser != null) {
		// If logged in, fetch user details
		User user = userServiceImpl.getUserByEmail(loggedInUser.getUsername());
		model.addAttribute("user", user);  // Pass user object to the view
	} else {
		// If not logged in, provide a new UserRegistration for the form
		model.addAttribute("user", new UserRegistrationDto());
	}

	return "registersection/registration";
}


	// User Registration Submission
	// User Registration Submission
	@PostMapping
	public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto registrationDto) {


		if (userService.emailExists(registrationDto.getEmail())) {
			return "redirect:registration?error";
		}
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
