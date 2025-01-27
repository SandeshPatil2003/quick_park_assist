package com.quickparkassist.controller;


import com.quickparkassist.dto.VehicleDto;
import com.quickparkassist.model.Vehicle;
import com.quickparkassist.repository.VehicleRepository;
import com.quickparkassist.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.quickparkassist.model.User;
import com.quickparkassist.service.UserServiceImpl;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class MainController {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private VehicleRepository vehicleRepository;

    @GetMapping("/login")
    public String login() {
        return "index";
    }

    @GetMapping("/register")
    public String register() {
        return "registersection/registeration";
    }

    @GetMapping("")
    public String home() {
        return "index";
    }

    @GetMapping("/delete")
    public String delete(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        String username = userDetails.getUsername();  // Get logged-in username
        User user = userService.findByUsername(username);  // Fetch user data
        model.addAttribute("user", user);
        return "registersection/delete";
    }

@GetMapping("/users")
public String userProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
    String username = userDetails.getUsername();  // Get logged-in username
    User user = userService.findByUsername(username);  // Fetch user data

    model.addAttribute("user", user);
    return "registersection/users";
}

@PostMapping("/update")
public String updateUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
    User existingUser = userService.findUserById(user.getId()); // Fetch the existing user

    // Handle password update
    if (user.getPassword() != null && !user.getPassword().isEmpty()) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    } else {
        user.setPassword(existingUser.getPassword()); // Preserve old password
    }

    // ✅ Preserve existing vehicles to avoid breaking foreign key constraints
    user.setVehicles(existingUser.getVehicles());

    userService.saveUser(user);

    // Success message
    redirectAttributes.addFlashAttribute("message", "Profile updated successfully.");

    return "redirect:/users";
}

    @GetMapping("/edit/{email}")
    public String editUserForm(@PathVariable String email, Model model) {
        User user = userService.getUserByEmail(email);
        model.addAttribute("user", user);
        return "registersection/edit";
    }

    @GetMapping("/remove/{email}")
    public String deleteUser(@PathVariable String email) {
        userService.deleteUserByEmail(email);
        return "redirect:/logout";
    }




    @GetMapping("/")
    public String viewUserProfile(Model model, @AuthenticationPrincipal UserDetails loggedInUser) {
        if (loggedInUser != null) {
            // Fetch logged-in user details using email
            User user = userService.getUserByEmail(loggedInUser.getUsername());
            model.addAttribute("user", user); // Pass only this user to the view
        } else {
            model.addAttribute("user", null); // No user is logged in
        }
        return "index"; // Adjust to the correct template name
    }

    @GetMapping("/add-vehicle")
    public String showAddVehiclePage() {
        return "registersection/add-vehicle";  // Maps to add-vehicle.html
    }


    @PostMapping("/add-vehicle")
    public String addVehicle(@ModelAttribute("vehicle") VehicleDto vehicleDto, RedirectAttributes redirectAttributes) {
        vehicleService.save(vehicleDto);
        redirectAttributes.addFlashAttribute("message", "Vehicle added successfully.");
        return "redirect:/registration";
    }
//
// Delete vehicle
@PostMapping("/vehicles/delete/{id}")
public String deleteVehicle(@PathVariable Long id,  RedirectAttributes redirectAttributes) {
    vehicleService.deleteVehicleById(id);
    // Get the currently logged-in user's email to fetch their updated vehicle list
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName();

    // Fetch the updated list of vehicles for the logged-in user
    List<Vehicle> vehicles = vehicleRepository.findByUserEmail(email);

    // Add the updated list of vehicles to the redirect attributes
    redirectAttributes.addFlashAttribute("vehicles", vehicles);
    redirectAttributes.addFlashAttribute("message", "Vehicle deleted successfully.");
    return "redirect:/view-vehicle"; // Redirect back to the vehicle list
}


@GetMapping("/view-vehicle")
public String getMyVehicles(Model model) {
    // Get the currently logged-in user's email
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName();

    // Fetch vehicles for the current user
    List<Vehicle> vehicles = vehicleRepository.findByUserEmail(email);

    // Add vehicles to the model
    model.addAttribute("vehicles", vehicles);

    return "registersection/view-vehicle";
//
}
}
