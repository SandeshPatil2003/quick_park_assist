package com.example.registration.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.registration.model.User;
import com.example.registration.service.UserServiceImpl;

@Controller
public class MainController {
    @Autowired
    private UserServiceImpl userService;

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
    public String delete() {
        return "registersection/delete";
    }

    @GetMapping("/users")
    public String viewUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "registersection/users";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute User user) {
        userService.saveUser(user);
        return "redirect:/users";
    }

    // @GetMapping("/edit/{id}")
    // public String editUserForm(@PathVariable Long id, Model model) {
    // User user = userService.getUserById(id);
    // model.addAttribute("user", user);
    // return "registersection/edit";
    // }

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







}
