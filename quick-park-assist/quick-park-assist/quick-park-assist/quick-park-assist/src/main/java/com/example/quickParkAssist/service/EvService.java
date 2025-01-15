package com.example.quickParkAssist.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.quickParkAssist.model.Evmodel;
import com.example.quickParkAssist.repository.EvRepository;

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
