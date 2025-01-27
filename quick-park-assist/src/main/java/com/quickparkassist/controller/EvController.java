package com.quickparkassist.controller;


import com.quickparkassist.model.*;
import com.quickparkassist.model.Evmodel;
import com.quickparkassist.model.Spot;
import com.quickparkassist.model.User;
import com.quickparkassist.model.Vehicle;
import com.quickparkassist.repository.SpotRepository;
import com.quickparkassist.repository.VehicleRepository;
import com.quickparkassist.service.*;
import com.quickparkassist.service.EVChargingStationService;
import com.quickparkassist.service.EvService;
import com.quickparkassist.service.SpotService;
import com.quickparkassist.service.UserServiceImpl;
import com.quickparkassist.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/reservations")
public class EvController {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private SpotRepository spotRepository;

    @Autowired
    private SpotService spotService;

    @Autowired
    private VehicleRepository vehicleRepository;
//@Autowired
//private UserService userService;

    @Autowired
    private EVChargingStationService chargingStationService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));


    }

    @GetMapping({"/create", "", "/"})
    public String createReservationForm(Model model, @AuthenticationPrincipal UserDetails loggedInUser) {
        if (loggedInUser != null) {
            // Fetch logged-in user details using email
            User user = userService.getUserByEmail(loggedInUser.getUsername());
            model.addAttribute("user", user); // Pass the logged-in user to the view

            // Fetch vehicles for the current user where hasElectric == "YES"
            List<Vehicle> electricVehicles = vehicleRepository.findByUserEmailAndHasElectric(user.getEmail(), "YES");

            // Add electric vehicles to the model
            model.addAttribute("vehicles", electricVehicles);
        } else {
            model.addAttribute("user", null); // No user is logged in
            model.addAttribute("vehicles", Collections.emptyList()); // Empty vehicle list
        }

        // Add a new reservation model attribute
        model.addAttribute("reservation", new Evmodel());

        // Fetch all parking spots where spot_type is 'ev'
        List<Spot> evSpots = spotService.getSpotsByType("ev");
        model.addAttribute("evSpots", evSpots);

        return "reservations/create";
    }


    @Autowired
    private EvService evService;

    @GetMapping("/view")
    public String viewAllReservations(Model model) {
        // Get the email (username) of the current user from UserContext
        String email = UserContext.getCurrentUsername();

        // Fetch the userId using the email
        Long userId = userService.findUserIdByUsername(email);

//        Long testId = userId;

        System.out.println(userId);
        if (userId == null) {
            // Handle the case where userId is null, maybe show an error page or return an empty list
            model.addAttribute("reservations", new ArrayList<>()); // No reservations available
            return "reservations/ "; // Or redirect to an error page
        }

        // List<Evmodel> reservations = evService.getAllReservations();

        // Get all reservations and filter them by userId
        List<Evmodel> reservations = evService.getAllReservations().stream()
                .filter(reservation -> reservation.getUserId().equals(userId)) // Only include reservations for the logged-in user
                .collect(Collectors.toList());

        model.addAttribute("reservations", reservations);
        return "reservations/view";
    }

    @GetMapping("/edit_ev")
    public String edit_ev(Model model) {
        // Get the email (username) of the current user from UserContext
        String email = UserContext.getCurrentUsername();

        // Fetch the userId using the email
        Long userId = userService.findUserIdByUsername(email);

        // List<Evmodel> reservations = evService.getAllReservations();

        // Get all reservations and filter them by userId
        List<Evmodel> reservations = evService.getAllReservations().stream()
                .filter(reservation -> reservation.getUserId().equals(userId)) // Only include reservations for the logged-in user
                .collect(Collectors.toList());
        model.addAttribute("reservations", reservations);
        return "reservations/edit_page";
    }

    @GetMapping("/delete_ev")
    public String delete_ev(Model model) {
        // Get the email (username) of the current user from UserContext
        String email = UserContext.getCurrentUsername();

        // Fetch the userId using the email
        Long userId = userService.findUserIdByUsername(email);

        //  List<Evmodel> reservations = evService.getAllReservations();

        // Get all reservations and filter them by userId
        List<Evmodel> reservations = evService.getAllReservations().stream()
                .filter(reservation -> reservation.getUserId().equals(userId)) // Only include reservations for the logged-in user
                .collect(Collectors.toList());
        model.addAttribute("reservations", reservations);
        return "reservations/delete_page";
    }



    @PostMapping("/save")
    public String saveReservation(@ModelAttribute("reservation") Evmodel reservation, RedirectAttributes redirectAttributes) {
        // Get the email (username) of the current user from UserContext
        String email = UserContext.getCurrentUsername();

        // Fetch the userId using the email
        Long userId = userService.findUserIdByUsername(email);

        // Set the userId on the reservation model
        reservation.setUserId(userId);
        // Assuming start_time is a LocalTime object and you have a setter for it
        LocalTime startTime = LocalTime.parse(reservation.getStart_time());

        // Calculate the end_time based on duration
        LocalTime endTime = reservation.calculateEndTime(startTime);


        reservation.setEnd_time(String.valueOf(endTime)); // If you still need to store it
        Long spotId = reservation.getEvSpot();
        Spot spot = spotService.getSpotById(spotId);

        if (spot != null) {
            // Set the spot details (name and location) on the reservation
            reservation.setSpotName(spot.getSpotName());
            reservation.setLocation(spot.getLocation());
        } else {
            // Handle the case when the spot is not found, for example, add an error message
            redirectAttributes.addFlashAttribute("errorMessage", "Selected EV spot not found.");
            return "redirect:/reservations";
        }

        evService.saveReservation(reservation);

        redirectAttributes.addFlashAttribute("message", "Your charging slot has been successfully reserved.");
        return "redirect:/reservations";
    }

    @GetMapping("/edit/{id}")
    public String editReservationForm(@PathVariable Long id,
                                      Model model,
                                      @AuthenticationPrincipal UserDetails loggedInUser) {
        // Fetch the reservation by its ID
        Evmodel reservation = evService.getReservationById(id);
        model.addAttribute("reservation", reservation);

        // Check if a user is logged in
        if (loggedInUser != null) {
            // Fetch logged-in user details using email
            User user = userService.getUserByEmail(loggedInUser.getUsername());
            model.addAttribute("user", user); // Pass the logged-in user to the view

            // Fetch vehicles for the current user where hasElectric == "YES"
            List<Vehicle> electricVehicles = vehicleRepository.findByUserEmailAndHasElectric(user.getEmail(), "YES");
            model.addAttribute("vehicles", electricVehicles); // Add electric vehicles to the model
        } else {
            model.addAttribute("user", null); // No user is logged in
            model.addAttribute("vehicles", Collections.emptyList()); // Empty vehicle list
        }

        // Fetch all parking spots where spot_type is 'ev'
        List<Spot> evSpots = spotService.getSpotsByType("ev");
        model.addAttribute("evSpots", evSpots);

        return "reservations/update";
    }


    @PostMapping("/update/{id}")
    public String updateReservation(@ModelAttribute("reservation") Evmodel reservation) {
        // Get the email (username) of the current user from UserContext
        String email = UserContext.getCurrentUsername();

        // Fetch the userId using the email
        Long userId = userService.findUserIdByUsername(email);

        // Check if the userId is not null before setting it
        if (userId != null) {
            reservation.setUserId(userId); // Set the userId in the reservation object
        } else {
            // Handle the case where userId is null (perhaps redirect to login or error page)
            return "redirect:/"; // Example: redirect to login page if user is not found
        }
        // Fetch the evSpot using the spotId and set it in the reservation object
        Long spotId = reservation.getEvSpot();  // Assuming evSpot is the spotId
        Spot spot = spotService.getSpotById(spotId);  // Fetch the spot object by id
        if (spot != null) {
            reservation.setSpotName(spot.getSpotName()); // Set the spotName in the reservation
        }
// Parse the start time as LocalTime (only time, no date)
        LocalTime startTime = LocalTime.parse(reservation.getStart_time());  // This will handle "17:30"

// Calculate the end time based on the start time (assuming the method requires LocalTime)
        LocalTime endTime = reservation.calculateEndTime(startTime);

// Set the calculated end time (time only, no date involved)
        reservation.setEnd_time(endTime.toString());  // Store only the time part (e.g., "21:20")


        evService.saveReservation(reservation);
        return "redirect:/reservations/edit_ev";
    }

    @GetMapping("/delete/{id}")
    public String deleteReservation(@PathVariable Long id) {
        evService.deleteReservation(id);
        return "redirect:/reservations/delete_ev";
    }
}
