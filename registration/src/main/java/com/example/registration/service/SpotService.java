package com.example.registration.service;

import com.example.registration.model.Spot;
import com.example.registration.repository.SpotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpotService {

    @Autowired
    private SpotRepository spotRepository;

    // Get all available spots
    public List<Spot> getAvailableSpots() {
        return spotRepository.findByAvailability("yes");
    }

    // Get all unavailable spots
    public List<Spot> getUnavailableSpots() {
        return spotRepository.findByAvailability("no");
    }

    // Get spots by both location and availability
    public List<Spot> getSpotsByLocationAndAvailability(String location, String availability) {
        return spotRepository.findByLocationAndAvailability(location, availability);
    }

    // Get spots by location only (ignoring availability)
    public List<Spot> getSpotsByLocation(String location) {
        return spotRepository.findByLocation(location);
    }

    // Get all spots ordered by location
    public List<Spot> getAllSpotsOrderedByLocation() {
        return spotRepository.findAllByOrderByLocationAsc();
    }

    // Get a spot by its ID
    public Spot getSpotById(Long id) {
        return spotRepository.findById(id).orElse(null);
    }

    // Save a new spot
    public void saveSpot(Spot spot) {
        spotRepository.save(spot);
    }

    // Update an existing spot
    public void updateSpot(Spot spot) {
        spotRepository.save(spot);
    }

    // Remove a spot by ID
    public void removeSpot(Long id) {
        spotRepository.deleteById(id);
    }
}
