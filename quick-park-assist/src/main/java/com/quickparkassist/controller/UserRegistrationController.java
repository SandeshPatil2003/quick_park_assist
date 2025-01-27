package com.quickparkassist.controller;

import com.quickparkassist.model.User;
import com.quickparkassist.service.UserServiceImpl;
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

import com.quickparkassist.dto.UserRegistrationDto;
import com.quickparkassist.dto.VehicleDto;
import com.quickparkassist.service.UserService;
import com.quickparkassist.service.VehicleService;

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

	@PostMapping
	public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto registrationDto) {


		if (userService.emailExists(registrationDto.getEmail())) {
			return "redirect:registration?error";
		}
		userService.save(registrationDto);
		return "redirect:/registration?success";
	}


}
