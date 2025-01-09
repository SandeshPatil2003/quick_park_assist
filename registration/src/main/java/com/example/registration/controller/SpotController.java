package com.example.registration.controller;

import com.example.registration.model.Spot;
import com.example.registration.repository.SpotRepository;
import com.example.registration.service.SpotService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class SpotController {

    @Autowired
    private SpotService spotService;

    @GetMapping("/spot-management")
    public String home(Model model) {
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
        model.addAttribute("availableSpots", spotService.getAvailableSpots());
        return "spotmanagement/availableSpots";
    }

    @GetMapping("/edit")
    public String edit(Model model) {
        model.addAttribute("availableSpots", spotService.getAvailableSpots());
        model.addAttribute("unavailableSpots", spotService.getUnavailableSpots());
        return "spotmanagement/editSpots";
    }

    @GetMapping("/remove")
    public String remove(Model model) {
        model.addAttribute("unavailableSpots", spotService.getUnavailableSpots());
        return "spotmanagement/unavailableSpots";
    }

    @PostMapping("/addSpot")
    public String addSpot(@ModelAttribute Spot spot) {
        spotService.saveSpot(spot);
        return "redirect:/add";
    }

    @GetMapping("/search")
    public String search(Model model) {
        return "spotmanagement/searchSpot";
    }

    @PostMapping("/searchspots")
    public String searchSpots(@RequestParam String location, @RequestParam String availability, Model model) {
        model.addAttribute("spots", spotService.getSpotsByLocationAndAvailability(location, availability));
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
    public String removeSpot(@RequestParam Long id) {
        spotService.removeSpot(id);
        return "redirect:/remove";
    }

}
