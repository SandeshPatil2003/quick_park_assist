package com.example.registration.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import com.example.registration.model.Evmodel;
import com.example.registration.service.EvService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/reservations")
public class EvController {



    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));


    }

    @GetMapping({"/create", "", "/"})
    public String createReservationForm(Model model) {
        model.addAttribute("reservation", new Evmodel());
        return "reservations/create";
    }
    
    @Autowired
    private EvService evService;

    @GetMapping("/view")
    public String viewAllReservations(Model model) {
        List<Evmodel> reservations = evService.getAllReservations();
        model.addAttribute("reservations", reservations);
        return "reservations/view";
    }

    @GetMapping("/edit_ev")
    public String edit_ev(Model model) {
        List<Evmodel> reservations = evService.getAllReservations();
        model.addAttribute("reservations", reservations);
        return "reservations/edit_page";
    }

    @GetMapping("/delete_ev")
    public String delete_ev(Model model) {
        List<Evmodel> reservations = evService.getAllReservations();
        model.addAttribute("reservations", reservations);
        return "reservations/delete_page";
    }

    

    @PostMapping("/save")
    public String saveReservation(@ModelAttribute("reservation") Evmodel reservation) {
        evService.saveReservation(reservation);
        return "redirect:/reservations";
    }

    @GetMapping("/edit/{id}")
    public String editReservationForm(@PathVariable Long id, Model model) {
        Evmodel reservation = evService.getReservationById(id);
        model.addAttribute("reservation", reservation);
        return "reservations/update";
    }

    @PostMapping("/update")
    public String updateReservation(@ModelAttribute("reservation") Evmodel reservation) {
        evService.saveReservation(reservation);
        return "redirect:/reservations/edit_ev";
    }

    @GetMapping("/delete/{id}")
    public String deleteReservation(@PathVariable Long id) {
        evService.deleteReservation(id);
        return "redirect:/reservations/delete_ev";
    }
}
