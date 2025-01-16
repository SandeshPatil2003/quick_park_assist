package com.example.quickParkAssist.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
// import java.util.Collections;
// import java.util.Optional;
import java.util.stream.Collectors;

import com.example.quickParkAssist.model.User;
import com.example.quickParkAssist.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.quickParkAssist.model.AddonService;
import com.example.quickParkAssist.model.AddonServiceBooking;
import com.example.quickParkAssist.repository.addonRepository;
import com.example.quickParkAssist.repository.addonServiceBookingRepository;

import lombok.Getter;
import lombok.Setter;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class addonController {
    @Autowired
    private addonRepository eRepo;
    @Autowired
    private addonServiceBookingRepository bookingRepo;
    @Autowired
    private UserServiceImpl userService;

    @GetMapping({ "/addAddonServices" })
    public ModelAndView addAddonServices(Model model, @AuthenticationPrincipal UserDetails loggedInUser) {
        if (loggedInUser != null) {
            // Fetch logged-in user details using email
            User user = userService.getUserByEmail(loggedInUser.getUsername());
            model.addAttribute("user", user); // Pass only this user to the view
        } else {
            model.addAttribute("user", null); // No user is logged in
        }
        ModelAndView mav = new ModelAndView("addonService/index");
        AddonService newAddon = new AddonService();
        mav.addObject("addon", newAddon);
        return mav;
    }

    @PostMapping({ "/saveAddonServices" })
    public String createAddonServices(@ModelAttribute AddonService newAddon,  RedirectAttributes redirectAttributes) {
        eRepo.save(newAddon);
        // Add success message
        redirectAttributes.addFlashAttribute("message", "Addon service added successfully!");

        return "redirect:/addAddonServices";
    }

    @GetMapping({ "/viewAllAddonServices" })
    public ModelAndView viewAllAddonServices(Model model, @AuthenticationPrincipal UserDetails loggedInUser) {
        if (loggedInUser != null) {
            // Fetch logged-in user details using email
            User user = userService.getUserByEmail(loggedInUser.getUsername());
            model.addAttribute("user", user); // Pass only this user to the view
        } else {
            model.addAttribute("user", null); // No user is logged in
        }
        ModelAndView mav = new ModelAndView("addonService/view-all-addon-services");
        List<AddonService> list = eRepo.findAll();
        mav.addObject("addon", list);
        return mav;
    }

    @Getter
    @Setter
    public class AddonRequest {
        private Long addonID;
        private Float duration;
    }

    @ResponseBody
    @PostMapping("/selectedAddonService")
    public ResponseEntity<String> processSelectedAddons(
            @RequestParam String serviceIDs,
            @RequestParam String durations) {

        try {
            AddonServiceBooking newBooking = AddonServiceBooking.builder()
                    .serviceIDs(serviceIDs)
                    .quantities(durations)
                    .build();
            bookingRepo.save(newBooking);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing the request: " + e.getMessage());
        }

        return ResponseEntity.ok("Addon services booked successfully.");
    }

    @GetMapping("/modifySelectedAddonServices")
    public ModelAndView modifyView(Model model, @AuthenticationPrincipal UserDetails loggedInUser) {
        if (loggedInUser != null) {
            // Fetch logged-in user details using email
            User user = userService.getUserByEmail(loggedInUser.getUsername());
            model.addAttribute("user", user); // Pass only this user to the view
        } else {
            model.addAttribute("user", null); // No user is logged in
        }
        ModelAndView mav = new ModelAndView("addonService/modify-addon-services");

        List<AddonServiceBooking> bookingList = bookingRepo.findAll();
        List<AddonService> allServices = eRepo.findAll();

        // Convert list of all services into a Map for quick lookup
        Map<Long, AddonService> serviceMap = allServices.stream()
                .collect(Collectors.toMap(AddonService::getServiceID, service -> service));

        // Prepare a structure to hold filtered services for each booking
        List<Map<String, Object>> addonBookingData = new ArrayList<>();

        for (AddonServiceBooking booking : bookingList) {
            String[] serviceIDsArray = booking.getServiceIDs().split(",");
            String[] quantitiesArray = booking.getQuantities().split(",");

            List<Map<String, Object>> servicesWithQuantities = new ArrayList<>();

            for (int i = 0; i < serviceIDsArray.length; i++) {
                String serviceIDStr = serviceIDsArray[i].trim();
                if (!serviceIDStr.isEmpty()) {
                    Long serviceID = Long.parseLong(serviceIDsArray[i].trim());

                    double quantity = Double.parseDouble(quantitiesArray[i].trim()); // Works for "0.0"

                    // Fetch service details if it exists
                    if (serviceMap.containsKey(serviceID)) {
                        Map<String, Object> serviceData = new HashMap<>();
                        serviceData.put("service", serviceMap.get(serviceID));
                        serviceData.put("quantity", quantity);
                        servicesWithQuantities.add(serviceData);
                    }
                }
            }

            // Store each booking's data along with its filtered services
            Map<String, Object> bookingData = new HashMap<>();
            bookingData.put("booking", booking);
            bookingData.put("services", servicesWithQuantities);

            addonBookingData.add(bookingData);
        }

        mav.addObject("addonBookingData", addonBookingData);
        return mav;
    }

    @GetMapping("/modifyAddonService")
    public ModelAndView modifyAddonService(@RequestParam Long serviceID) {
        ModelAndView mav = new ModelAndView("addonService/modify-addon-price");

        Optional<AddonService> optionalService = eRepo.findById(serviceID);
        if (optionalService.isPresent()) {
            mav.addObject("service", optionalService.get());
        } else {
            mav.addObject("error", "Service not found.");
        }

        return mav;
    }
    
    @PostMapping("/updateAddonServicePrice")
    public String updateAddonServicePrice(@RequestParam Long serviceID,
            @RequestParam float newPrice,
            RedirectAttributes redirectAttributes) {
        Optional<AddonService> optionalService = eRepo.findById(serviceID);

        if (optionalService.isPresent()) {
            AddonService service = optionalService.get();
            service.setPrice((float)newPrice);
            eRepo.save(service);
            redirectAttributes.addFlashAttribute("success", "Price updated successfully.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Service not found.");
        }

        return "redirect:/modifySelectedAddonServices";
    }

    @GetMapping("/modifyAddonServiceDuration")
    public ModelAndView showModifyDurationPage(@RequestParam Long serviceID) {
        ModelAndView mav = new ModelAndView("addonService/modify-addon-service-duration");

        Optional<AddonService> optionalService = eRepo.findById(serviceID);
        if (optionalService.isPresent()) {
            mav.addObject("service", optionalService.get());
        } else {
            mav.addObject("error", "Service not found.");
        }
        
        return mav;
    }

    @PostMapping("/updateAddonServiceDuration")
    public String updateAddonServiceDuration(@RequestParam Long serviceID,
                                             @RequestParam float newDuration,
                                             RedirectAttributes redirectAttributes) {
        Optional<AddonService> optionalService = eRepo.findById(serviceID);

        if (optionalService.isPresent()) {
            AddonService service = optionalService.get();
            service.setDuration(newDuration); // Ensure setDuration uses Double
            eRepo.save(service);
            redirectAttributes.addFlashAttribute("success", "Duration updated successfully.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Service not found.");
        }

        return "redirect:/modifySelectedAddonServices";
    }

    // Delete an individual service from a booking
    @PostMapping("/deleteService")
    public String deleteService(@RequestParam Long bookingID, @RequestParam Long serviceID, RedirectAttributes redirectAttributes) {
        Optional<AddonServiceBooking> optionalBooking = bookingRepo.findById(bookingID);

        if (optionalBooking.isPresent()) {
            AddonServiceBooking booking = optionalBooking.get();
            String[] serviceIDsArray = booking.getServiceIDs().split(",");
            String[] quantitiesArray = booking.getQuantities().split(",");

            List<String> newServiceIDs = new ArrayList<>();
            List<String> newQuantities = new ArrayList<>();

            for (int i = 0; i < serviceIDsArray.length; i++) {
                if (!serviceIDsArray[i].trim().equals(serviceID.toString())) {
                    newServiceIDs.add(serviceIDsArray[i]);
                    newQuantities.add(quantitiesArray[i]);
                }
            }

            // Update the booking details
            booking.setServiceIDs(String.join(",", newServiceIDs));
            booking.setQuantities(String.join(",", newQuantities));

            bookingRepo.save(booking);
            redirectAttributes.addFlashAttribute("message", "Service deleted successfully!");
        }

        return "redirect:/modifySelectedAddonServices";
    }

    // Delete an entire booking
    @PostMapping("/deleteBooking")
    public String deleteBooking(@RequestParam Long bookingID, RedirectAttributes redirectAttributes) {
        if (bookingRepo.existsById(bookingID)) {
            bookingRepo.deleteById(bookingID);
            redirectAttributes.addFlashAttribute("message", "Booking deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Booking not found!");
        }

        return "redirect:/modifySelectedAddonServices";
    }

    @GetMapping("/removeAddonServices")
    public ModelAndView removeAddonServices(Model model, @AuthenticationPrincipal UserDetails loggedInUser) {
        if (loggedInUser != null) {
            // Fetch logged-in user details using email
            User user = userService.getUserByEmail(loggedInUser.getUsername());
            model.addAttribute("user", user); // Pass only this user to the view
        } else {
            model.addAttribute("user", null); // No user is logged in
        }
        ModelAndView mav = new ModelAndView("addonService/remove-addon-services");
        List<AddonService> list = eRepo.findAll();
        mav.addObject("addonServices", list);
        return mav;
    }

    @PostMapping("/deleteAddonService")
    public String deleteAddonService(@RequestParam Long serviceID, RedirectAttributes redirectAttributes) {
        Optional<AddonService> service = eRepo.findById(serviceID);
        if (service.isPresent()) {
            eRepo.deleteById(serviceID);
            redirectAttributes.addFlashAttribute("message", "Addon service deleted successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Addon service not found.");
        }
        return "redirect:/removeAddonServices";
    }
}
