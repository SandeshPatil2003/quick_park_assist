package com.example.quickParkAssist.controller;


import com.example.quickParkAssist.model.User;
import com.example.quickParkAssist.model.Vehicle;
import com.example.quickParkAssist.repository.SpotRepository;
import com.example.quickParkAssist.model.Spot;
import com.example.quickParkAssist.repository.VehicleRepository;
import com.example.quickParkAssist.service.SpotService;

import com.example.quickParkAssist.model.Booking;
import com.example.quickParkAssist.service.BookingService;

import com.example.quickParkAssist.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



import com.example.quickParkAssist.util.UserContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


@Controller
public class BookingController {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private SpotRepository spotRepository;

    @Autowired
    private SpotService spotService;

    @Autowired
    private BookingService bookingService;
    @Autowired
    private VehicleRepository vehicleRepository;

//    @Autowired
//    private UserService userService; //userService from Login module




    // GET request for the homepage (list available parking spots)
//    @GetMapping("/")
//    public String index(Model model) {
//
//        // Fetch a list of available parking spots from the database
//       //List<ParkingSpot> parkingSpots = parkingSpotRepository.findAll();
//        // Fetch a list of available parking spots from the database
//        // Fetch a list of available parking spots from the database
//       List<Spot> availableParkingSpots = spotRepository.findByAvailability("YES");
//
//        // Log the fetched spots
//       // System.out.println("Available spots: " + availableParkingSpots);
//
//        model.addAttribute("parkingSpots", availableParkingSpots);  // Add spots to the model
//        return "booking/booking";  // Return the view
//    }

    // Handle GET request for the booking page
    @GetMapping("/booking")
    public String mainBookingPage(Model model, @AuthenticationPrincipal UserDetails loggedInUser) {
        if (loggedInUser != null) {
            // Fetch logged-in user details using email
            User user = userService.getUserByEmail(loggedInUser.getUsername());
            model.addAttribute("user", user); // Pass only this user to the view
            // Fetch non-electric vehicles using user ID
            List<Vehicle> nonElectricVehicles = vehicleRepository.findByUserAndHasElectric(user, "NO");

            model.addAttribute("vehicles", nonElectricVehicles);
        } else {
            model.addAttribute("user", null); // No user is logged in
            model.addAttribute("vehicles", null);

        }
        // Fetch a list of available parking spots from the database
        List<Spot> availableParkingSpots = spotRepository.findByAvailability("YES");
        model.addAttribute("parkingSpots", availableParkingSpots);

        return "booking/booking"; // Return the booking page
    }

    // Handle GET request for the book page
    @GetMapping("/book")
    public String booking(Model model, @AuthenticationPrincipal UserDetails loggedInUser) {

        if (loggedInUser != null) {
            // Fetch logged-in user details using email
            User user = userService.getUserByEmail(loggedInUser.getUsername());
            model.addAttribute("user", user); // Pass only this user to the view
            // Fetch non-electric vehicles using user ID
            List<Vehicle> nonElectricVehicles = vehicleRepository.findByUserAndHasElectric(user, "NO");

            model.addAttribute("vehicles", nonElectricVehicles);
        } else {
            model.addAttribute("user", null); // No user is logged in
            model.addAttribute("vehicles", null);

        }
        // Fetch a list of available parking spots from the database
        List<Spot> availableParkingSpots =spotRepository.findByAvailability("YES");

        model.addAttribute("parkingSpots", availableParkingSpots);

        return "booking/booking"; // Return the booking page view
    }


    @GetMapping("/modify-Booking-Details")
    public String modifyBookingDetails(Model model) {


        String email = UserContext.getCurrentUsername();

        // Fetch the user ID using the username
        Long userId = userService.findUserIdByUsername(email);

        bookingService.updateBookingStatuses();

        // Fetch bookings for the logged-in user with statuses "Confirmed" or "Pending"
        List<Booking> bookings = bookingService.findBookingsByUserIdAndStatuses(userId,  Arrays.asList(Booking.Status.CONFIRMED, Booking.Status.PENDING));

        // Place null startTime values at the end in descending order
     bookings.sort(Comparator.comparing(Booking::getStartTime, Comparator.nullsLast(Comparator.reverseOrder())));

      //  bookings.sort((b1, b2) -> b2.getStartTime().compareTo(b1.getStartTime()));

        model.addAttribute("bookings", bookings);
        return "booking/modify-booking-details";  // Return the view where bookings will be displayed
    }

@GetMapping("/view-Booking-Details")
public String viewBookingDetails(Model model) {


    // Get the current username
    String email = UserContext.getCurrentUsername();

    // Fetch the user ID using the username
    Long userId = userService.findUserIdByUsername(email);
    // Fetch bookings for the logged-in user with specific statuses
    List<Booking> bookings = bookingService.findBookingsByUserId(userId);

    // Sort bookings by startTime in descending order (most recent first)
    bookings.sort((b1, b2) -> b2.getStartTime().compareTo(b1.getStartTime()));

    model.addAttribute("bookings", bookings);
    return "booking/view-booking";  // Return the view where bookings will be displayed
}


    // Show the edit form with the current booking details
    @GetMapping("/editBooking/{id}")
    public String editBooking(@PathVariable("id") Long bookingId, Model model) {

        Booking booking = bookingService.findById(bookingId).orElse(null);

        if (booking == null) {
            // Handle booking not found (optional)
            model.addAttribute("message", "Booking not found");
            return "booking/view-booking";
        }

        // Fetch all parking spots
      //  List<Spot> availableParkingSpots = spotRepository.findByAvailability("YES");

        List<Spot> allParkingSpots = spotRepository.findAll();

        List<Vehicle> vehicles = vehicleRepository.findAll(); // Fetch all vehicles for the dropdown

       // model.addAttribute("parkingSpots", availableParkingSpots);

        model.addAttribute("parkingSpots", allParkingSpots); // Provide all parking spots
        model.addAttribute("vehicles", vehicles);
        model.addAttribute("booking", booking);
        //model.addAttribute("parkingSpots", parkingSpots); // Pass the parking spots to the view

        //model.addAttribute("booking", booking);
        return "booking/edit-booking";
    }



@PostMapping("/updateBooking/{id}")
public String updateBooking(@PathVariable("id") Long bookingId,
                            @ModelAttribute Booking booking,
                            @RequestParam String parkingSpotLocation,
//                          @RequestParam String startTime,
                            @RequestParam Long vehicleId,
                            RedirectAttributes redirectAttributes,

                            Model model) {

    try {

            // Parse startTime and set it on the booking object

//            LocalDateTime parsedStartTime = LocalDateTime.parse(startTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
//            booking.setStartTime(parsedStartTime);

            Long spotId = Long.valueOf(parkingSpotLocation);
            Spot spot = spotRepository.findById(spotId).orElse(null);
            
            if (spot == null) {
                model.addAttribute("message", "Parking spot not found.");
                return "booking/edit-booking";
            }

        // Fetch the selected vehicle
        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElse(null); // Fetch the vehicle by its ID

        if (vehicle == null) {
            model.addAttribute("message", "Vehicle not found.");
            return "booking/edit-booking"; // Handle case when vehicle is not found
        }

        if (!bookingId.equals(booking.getId())) {
                model.addAttribute("message", "Invalid booking ID.");

                return "booking/edit-booking";
            }

            // Set the selected parking spot in the booking
        //    Spot spot = spotRepository.findById(spotId).orElse(null);
//            if (spot == null) {
//                model.addAttribute("message", "Parking spot not found.");
//                return "booking/edit-booking"; // Return back to the edit form if spot is not found
//            }

// Set the parking spot object in the booking
            booking.setSpot(spot);

            // Save the location of the selected parking spot in the booking
            booking.setLocation(spot.getLocation());  // Save the location instead of spotId
        booking.setVehicle(vehicle);

        // Save the updated Booking
            bookingService.saveBooking(booking);

            // Redirect to view the updated booking

            redirectAttributes.addFlashAttribute("message", "Booking updated successfully");

            return "redirect:/modify-Booking-Details";
        }
    catch (DateTimeParseException e) {
        model.addAttribute("message", "Invalid start time format.");
        return "booking/edit-booking";
    }
        catch (NumberFormatException e) {
            model.addAttribute("message", "Invalid parking spot location.");
            return "booking/edit-booking";
        }
}

    @GetMapping("/viewBookingByNumber")
    public String viewBookingPage() {
        return "booking/view-booking-by-number";  // user enter their mobile number and find bookings
    }

    //process the form and fetch bookings by mobile number
    @GetMapping("/viewBooking-number")
    public String viewBooking(@RequestParam("mobileNumber") String mobileNumber, Model model) {
        bookingService.updateBookingStatuses();
        // Fetch the currently logged-in user's email/username
        String email = UserContext.getCurrentUsername();

        // Retrieve the user ID based on the username
        Long userId = userService.findUserIdByUsername(email);

        // Fetch the user's registered mobile number
        String registeredMobileNumber = userService.findMobileNumberByUserId(userId);
        // Check if the entered mobile number matches the user's registered number
//        if (!mobileNumber.equals(registeredMobileNumber)) {
//            // If not, show an error message
//            model.addAttribute("error", "Please enter your own registered mobile number.");
//            return "booking/view-booking-by-number";  // Return to the same page with error
//        }
        // ✅ Check if entered number matches the user's registered number
        // ✅ OR if the booking with that number belongs to the user
        boolean isOwnerOfBooking = bookingService.existsByMobileNumberAndUserId(mobileNumber, userId);

        if (!mobileNumber.equals(registeredMobileNumber) && !isOwnerOfBooking) {
            model.addAttribute("error", "Please enter your own registered mobile number.");
            return "booking/view-booking-by-number";  // Stay on the same page with error
        }


        // Fetch bookings based on the mobile number
        List<Booking> bookings = bookingService.findByMobileNumber(mobileNumber);

        // Sort bookings by startTime in descending order (most recent first)
        bookings.sort((b1, b2) -> b2.getStartTime().compareTo(b1.getStartTime()));


        model.addAttribute("bookings", bookings);  // Add bookings to the model

        model.addAttribute("message", "Here are your booking details for the entered mobile number.");

        // Return the same page (viewBookingForm.html) with the booking details
        return "booking/view-booking-by-number";
    }
    //process the form and fetch bookings by mobile number
//    @GetMapping("/viewBooking-number")
//    public String viewBooking(@RequestParam("mobileNumber") String mobileNumber, Model model) {
//        bookingService.updateBookingStatuses();
//        // Fetch bookings based on the mobile number
//        List<Booking> bookings = bookingService.findByMobileNumber(mobileNumber);
//
//        // Sort bookings by startTime in descending order (most recent first)
//        bookings.sort((b1, b2) -> b2.getStartTime().compareTo(b1.getStartTime()));
//
//
//        model.addAttribute("bookings", bookings);  // Add bookings to the model
//
//        // Return the same page (viewBookingForm.html) with the booking details
//        return "booking/view-booking-by-number";
//    }
    @GetMapping("/viewBookingToCancel")
    public String viewBookings(Model model) {

        String email = UserContext.getCurrentUsername();

        // Fetch the user ID using the username
        Long userId = userService.findUserIdByUsername(email);

        List<Booking> bookings = bookingService.findBookingsByUserIdAndStatuses(userId,  Arrays.asList(Booking.Status.CONFIRMED, Booking.Status.PENDING));
        model.addAttribute("bookings", bookings);
        return "booking/cancel-booking";
    }

    @PostMapping("/cancelBooking/{id}")
    public String cancelBooking(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        bookingService.updateBookingStatusTOCancel(id, Booking.Status.CANCELLED);

        // Fetch the associated booking to access the parking spot
        Booking booking = bookingService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid booking ID"));

        Spot spot = booking.getSpot();  // Retrieve the parking spot from the booking

        // Validate that the spot is not null
        if (spot == null) {
            throw new IllegalStateException("No associated parking spot found for booking ID: " + id);
        }
        // Update the parking spot's availability to "YES"

        spot.setAvailability("yes");
        spotRepository.save(spot);


         redirectAttributes.addFlashAttribute("message", "Booking canceled successfully");

        return "redirect:/viewBookingToCancel"; // Redirect to refresh the bookings list
    }


    // Handle POST request for booking parking spot
    @PostMapping("/book")
    public String bookParkingSpot(
            @RequestParam String fullName,
            @RequestParam String parkingSpotId, // Use spotId to reference parking spots
            @RequestParam int duration,
            @RequestParam String startTime,
            @RequestParam String price,
            @RequestParam String paymentMethod,
            @RequestParam String mobileNumber,
//            @RequestParam String vehicleInfo,
            @RequestParam Long vehicleId,
            Model model,
              RedirectAttributes redirectAttributes
    ) {

        try {

            String email = UserContext.getCurrentUsername();

            // Fetch the user ID using the username
            Long userId = userService.findUserIdByUsername(email);

            // Convert startTime string to LocalDateTime
            LocalDateTime startDateTime = LocalDateTime.parse(startTime);

            // Convert parkingSpotId (String) to Long
            Long parkingSpotIdLong = Long.valueOf(parkingSpotId);
            // Find the selected parking spot by its spotId using Spot repository
            Spot spot = spotRepository.findById(parkingSpotIdLong)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid parking spot ID"));

            // Fetch the selected vehicle
            Vehicle vehicle = vehicleRepository.findById(vehicleId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid vehicle ID"));



            // Split vehicleInfo into model and number
           // String[] vehicleDetails = vehicleInfo.split(" - ");
            //String vehicleModel = vehicleDetails[0].trim();  // Extract model
          //  String vehicleNumber = vehicleDetails[1].trim(); // Extract number

            // Calculate the price based on price per hour and duration
            double pricePerHour = spot.getPricePerHour();
            double totalPrice = pricePerHour * duration;

            // Create a new Booking object and set the values
            Booking booking = new Booking();
            booking.setFullName(fullName);
            booking.setMobileNumber(mobileNumber);
            booking.setSpot(spot); // Store the entire ParkingSpot object
            booking.setLocation(spot.getLocation()); // Set the parking spot location
            booking.setDuration(String.valueOf(duration)); // Convert int to String
            booking.setStartTime(startDateTime);
            booking.setPrice(String.format("%.2f", totalPrice)); // Store the price formatted as string
            booking.setPaymentMethod(paymentMethod);

            booking.setVehicle(vehicle);
//            booking.setVehicleModel(vehicleModel);   // Store vehicle model
//            booking.setVehicleNumber(vehicleNumber);
            // Set the status to "CONFIRMED" by default
           booking.setStatus(Booking.Status.CONFIRMED);
            booking.setUserId(userId); // Use the  user ID
            // Save the booking to the database
            bookingService.saveBooking(booking);

            // Update parking spot availability to "NO"
            spot.setAvailability("NO");
            spotRepository.save(spot);
            // Add confirmation message to the model
            redirectAttributes.addFlashAttribute("confirmationMessage", "Booking Confirmed! Your spot is reserved.");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to confirm booking: " + e.getMessage());

        }
        return "redirect:/booking";
    }
}
