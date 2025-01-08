package com.example.registration.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.registration.model.Evmodel;
import com.example.registration.repository.EvRepository;

import java.util.List;

@Service
public class EvService {

    @Autowired
    private EvRepository evRepository;

    public List<Evmodel> getAllReservations() {
        return evRepository.findAll();
    }

    public Evmodel getReservationById(Long id) {
        return evRepository.findById(id).orElse(null);
    }

    public Evmodel saveReservation(Evmodel reservation) {
        return evRepository.save(reservation);
    }

    public void deleteReservation(Long id) {
        evRepository.deleteById(id);
    }
}
