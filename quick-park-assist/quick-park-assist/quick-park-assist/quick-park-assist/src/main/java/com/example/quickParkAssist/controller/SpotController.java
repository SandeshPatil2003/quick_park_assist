package com.example.quickParkAssist.controller;

import com.example.quickParkAssist.model.Spot;
import com.example.quickParkAssist.model.User;
import com.example.quickParkAssist.repository.SpotRepository;
import com.example.quickParkAssist.service.SpotService;

import java.util.List;
import java.util.stream.Collectors;

import com.example.quickParkAssist.service.UserServiceImpl;
//import org.apache.catalina.User;
import com.example.quickParkAssist.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SpotController {

    @Autowired
    private SpotService spotService;

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/spot-management")
    public String home(Model model, @AuthenticationPrincipal UserDetails loggedInUser) {
        if (loggedInUser != null) {
            // Fetch logged-in user details using email
            User user = userService.getUserByEmail(loggedInUser.getUsername());
            model.addAttribute("user", user); // Pass only this user to the view
        } else {
            model.addAttribute("user", null); // No user is logged in
        }
        model.addAttribute("availableSpots", spotService.getAvailableSpots());
        model.addAttribute("unavailableSpots", spotService.getUnavailableSpots());
        return "spotmanagement/addSpots";
    }

    @GetMapping("/add")
    public String adding() {
        return "spotmanagement/addSpots";
    }

    @GetMapping("/available")
    public String available(Model model) {
        String email = UserContext.getCurrentUsername();
        Long userId = userService.findUserIdByUsername(email);
// Fetch the available spots owned by the logged-in user
        List<Spot> userSpots = spotService.getAvailableSpotsByUserId(userId);
        model.addAttribute("availableSpots", userSpots);

      //  model.addAttribute("availableSpots", spotService.getAvailableSpots());
        return "spotmanagement/availableSpots";
    }

    @GetMapping("/edit")
    public String edit(Model model) {
        // Get the currently logged-in user's email or username
        String email = UserContext.getCurrentUsername();

        // Fetch the user ID using the email/username
        Long userId = userService.findUserIdByUsername(email);
        // Get the spots for the logged-in user
        model.addAttribute("availableSpots", spotService.getAvailableSpotsByUser_Id(userId)); // Only available spots for the user
        model.addAttribute("unavailableSpots", spotService.getUnavailableSpotsByUserId(userId)); // Only unavailable spots for the user


//        model.addAttribute("availableSpots", spotService.getAvailableSpots());
//        model.addAttribute("unavailableSpots", spotService.getUnavailableSpots());
        return "spotmanagement/editSpots";
    }

    @GetMapping("/remove")
    public String remove(Model model) {
        model.addAttribute("unavailableSpots", spotService.getUnavailableSpots());
        return "spotmanagement/unavailableSpots";
    }

    @PostMapping("/addSpot")
    public String addSpot(@ModelAttribute Spot spot, RedirectAttributes redirectAttributes) {
        // Get the currently logged-in user's email/username
        String email = UserContext.getCurrentUsername();

        // Fetch the user ID using the email/username
        Long userId = userService.findUserIdByUsername(email);

        // Set the user ID to the spot
        spot.setUserId(userId);

        spotService.saveSpot(spot);
        redirectAttributes.addFlashAttribute("message", "Spot added successfully!");

        return "redirect:/add";
    }

    @GetMapping("/search")
    public String search(Model model) {
        return "spotmanagement/searchSpot";
    }

    @PostMapping("/searchspots")
    public String searchSpots(@RequestParam String location,
                              @RequestParam String availability,
                              @RequestParam("spotType") String spotType,
                              Model model) {
        String email = UserContext.getCurrentUsername();
        Long userId = userService.findUserIdByUsername(email);
        Long test=userId;
// Fetch the spots based on location, spot type, and availability
        List<Spot> spots = spotService.getSpotsByLocationAndAvailability(location, spotType, availability);

        // Check if the spots belong to the logged-in user
        List<Spot> userSpots = spots.stream()
                .filter(spot -> spot.getUserId() != null && spot.getUserId().equals(userId))
                .collect(Collectors.toList());
        if (userSpots.isEmpty()) {
            // No spots found for the current user, add a message to the model
            model.addAttribute("message", "Please enter a spot registered by you.");
            return "spotmanagement/searchSpot"; // Return to the search page with the message
        }

        // Add the user-specific spots to the model
        model.addAttribute("spots", userSpots);
      //  model.addAttribute("spots", spotService.getSpotsByLocationAndAvailability(location, spotType, availability));
        return "spotmanagement/searchResults";
    }

    @Autowired
    private SpotRepository spotRepository;

    @GetMapping("/fetchLocationSuggestions")
    @ResponseBody
    public List<String> fetchLocationSuggestions(@RequestParam("query") String query) {
        // Assuming SpotRepository has a method to fetch location based on the query
        List<String> locations = spotRepository.findLocationsByQuery(query);
        return locations;
    }

    @GetMapping("/editSpot/{spotId}")
    public String editSpot(@PathVariable Long spotId, Model model) {
        Spot spot = spotService.getSpotById(spotId);
        model.addAttribute("spot", spot);
        return "spotmanagement/editSpot";
    }

    @PostMapping("/modifySpot")
    public String modifySpot(@ModelAttribute Spot spot) {
        spotService.updateSpot(spot);
        return "redirect:/edit";
    }

    @PostMapping("/removeSpot")
    public String removeSpot(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        spotService.removeSpot(id);
        redirectAttributes.addFlashAttribute("message", "Spot removed successfully!");
        return "redirect:/remove";
    }

}
