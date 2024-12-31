package com.example.registration.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.registration.model.User;
import com.example.registration.service.UserServiceImpl;

@Controller
public class MainController {
    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/login")
    public String login() {
        return "registersection/login";
    }

    @GetMapping("/register")
    public String register() {
        return "registersection/register";
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


    // Handle DELETE request for user deletion by email
    // @DeleteMapping("/remove/{email}")
    // public String deleteUser(@PathVariable String email) {
    //     boolean isDeleted = userService.deleteUserByEmail(email);
    //     if (isDeleted) {
    //         return "User deleted successfully.";
    //     } else {
    //         return "User not found.";
    //     }
    // }

}
