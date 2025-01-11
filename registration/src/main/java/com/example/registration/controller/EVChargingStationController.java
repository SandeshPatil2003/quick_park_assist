//package com.example.registration.controller;
//
//
//import com.example.registration.model.EVChargingStation;
//import com.example.registration.service.EVChargingStationService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/charging-stations")
//public class EVChargingStationController {
//
//    @Autowired
//    private EVChargingStationService chargingStationService;
//
//    // Get all charging stations
//    @GetMapping
//    public List<EVChargingStation> getAllStations() {
//        return chargingStationService.getAllStations();
//    }
//
//    // Get a single charging station by ID
//    @GetMapping("/{id}")
//    public Optional<EVChargingStation> getStationById(@PathVariable Long id) {
//        return chargingStationService.getStationById(id);
//    }
//
//    // Add a new charging station
//    @PostMapping
//    public EVChargingStation addStation(@RequestBody EVChargingStation chargingStation) {
//        return chargingStationService.saveStation(chargingStation);
//    }
//
//    // Delete a charging station by ID
//    @DeleteMapping("/{id}")
//    public void deleteStation(@PathVariable Long id) {
//        chargingStationService.deleteStation(id);
//    }
//}
