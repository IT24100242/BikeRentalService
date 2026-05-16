package com.example.bikerental.controller;

import com.example.bikerental.model.Rental;
import com.example.bikerental.service.BikeService;
import com.example.bikerental.service.RentalService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;




@RestController

@RequestMapping("/api")
public class RentalController {

    
    @Autowired
    private RentalService rentalService;

    @Autowired
    private BikeService bikeService;

    
    @GetMapping("/rentals")
    public List<Rental> getAllRentals(HttpSession session) {
        
        
        return rentalService.getAllRentals();
    }

    
    @PostMapping("/rent")
    public Map<String, String> rentBike(@RequestParam("bikeId") String bikeId,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestParam("cost") double cost,
            HttpSession session) {

        
        Map<String, String> response = new HashMap<>();
        
        
        String userEmail = (String) session.getAttribute("userEmail");

        
        if (userEmail == null) {
            response.put("status", "error");
            response.put("message", "You must be logged in to rent a bike.");
            return response;
        }

        
        if (!bikeService.updateBikeAvailability(bikeId, false)) {
            
            response.put("status", "error");
            response.put("message", "Bike is no longer available.");
            return response;
        }

        
        String rentalId = "R-" + UUID.randomUUID().toString().substring(0, 5);
        
        
        Rental newRental = new Rental(rentalId, userEmail, bikeId, startDate, endDate, cost, "Confirmed");
        
        
        rentalService.createRental(newRental);

        
        response.put("status", "success");
        response.put("message", "Bike successfully booked!");
        return response;
    }

    
    @PostMapping("/return")
    public Map<String, String> returnBike(@RequestParam("bikeId") String bikeId, HttpSession session) {
        Map<String, String> response = new HashMap<>();
        String userEmail = (String) session.getAttribute("userEmail");
        
        
        if (userEmail == null) {
            response.put("status", "error");
            response.put("message", "You must be logged in to return a bike.");
            return response;
        }

        
        if (bikeService.updateBikeAvailability(bikeId, true)) {
            response.put("status", "success");
            response.put("message", "Bike successfully returned!");
        } else {
            response.put("status", "error");
            response.put("message", "Bike not found.");
        }
        
        return response;
    }
}
