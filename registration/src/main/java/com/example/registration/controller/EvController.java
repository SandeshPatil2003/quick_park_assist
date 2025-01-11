package com.example.registration.controller;


import com.example.registration.model.EVChargingStation;
import com.example.registration.service.EVChargingStationService;
import com.example.registration.service.UserService;
import com.example.registration.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import com.example.registration.model.Evmodel;
import com.example.registration.service.EvService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/reservations")
public class EvController {

@Autowired
private UserService userService;

@Autowired
private EVChargingStationService chargingStationService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));


    }

    @GetMapping({"/create", "", "/"})
    public String createReservationForm(Model model) {
        model.addAttribute("reservation", new Evmodel());
        // Fetch all charging stations and add them to the model
        List<EVChargingStation> chargingStations = chargingStationService.getAllStations();

//        List<EVChargingStation> chargingStations = chargingStationService.getAllStations();
       model.addAttribute("stations", chargingStations);
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

        // Save the reservation along with the calculated end_time
        reservation.setEnd_time(String.valueOf(endTime)); // If you still need to store it
        evService.saveReservation(reservation);

        redirectAttributes.addFlashAttribute("message", "Your charging slot has been successfully reserved.");
        return "redirect:/reservations";
    }

    @GetMapping("/edit/{id}")
    public String editReservationForm(@PathVariable Long id, Model model) {
        Evmodel reservation = evService.getReservationById(id);
        // Fetch all charging stations
        List<EVChargingStation> chargingStations = chargingStationService.getAllStations();

        // Add the charging stations and reservation to the model
        model.addAttribute("stations", chargingStations);
        model.addAttribute("reservation", reservation);
        return "reservations/update";
    }

    @PostMapping("/update")
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

        evService.saveReservation(reservation);
        return "redirect:/reservations/edit_ev";
    }

    @GetMapping("/delete/{id}")
    public String deleteReservation(@PathVariable Long id) {
        evService.deleteReservation(id);
        return "redirect:/reservations/delete_ev";
    }
}
