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

// @RestController is used to return data like json instead of html pages
@RestController
// @RequestMapping all endpoints in this class start with /api
@RequestMapping("/api")
public class RentalController {

    // dependency injection-spring automatically gives the needed service objects
    @Autowired
    private RentalService rentalService;

    @Autowired
    private BikeService bikeService;

    //read ope--> returns all rentals as a json list for the frontend
    @GetMapping("/rentals")
    public List<Rental> getAllRentals(HttpSession session) {
        //return all rentals from the text file
        // in a real app, we should check the session here for security
        return rentalService.getAllRentals();
    }
    //create and update Operation-- handles bike booking and returns status message as json
    @PostMapping("/rent")
    public Map<String, String> rentBike(@RequestParam("bikeId") String bikeId,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestParam("cost") double cost,
            HttpSession session) {

        //use a HashMap to dynamically build our JSON response
        Map<String, String> response = new HashMap<>();
        
        //retrieve the currently logged-in user's email from the session
        String userEmail = (String) session.getAttribute("userEmail");
        //data Validation: Ensure the user is actually logged in before allowing a rental
        if (userEmail == null) {
            response.put("status", "error");
            response.put("message", "You must be logged in to rent a bike.");
            return response;
        }
        //logical Validation & State Update: Check if the bike is still available, and if so, mark it as booked (false)
        if (!bikeService.updateBikeAvailability(bikeId, false)) {
            //if updateBikeAvailability returns false, the bike doesnt exist or we failed to write to the file
            response.put("status", "error");
            response.put("message", "Bike is no longer available.");
            return response;
        }
        //generate a random unique ID trans
        String rentalId = "R-" + UUID.randomUUID().toString().substring(0, 5);
        
        //instantiate a new Rental object (Object-Oriented Concept)
        Rental newRental = new Rental(rentalId, userEmail, bikeId, startDate, endDate, cost, "Confirmed");
        
        //save the transaction to our rentals.txt flat-file database
        rentalService.createRental(newRental);

        //send a success response back to the JavaScript frontend
        response.put("status", "success");
        response.put("message", "Bike successfully booked!");
        return response;
    }
    //update operation-->handles returning a bike to make it available again
    @PostMapping("/return")
    public Map<String, String> returnBike(@RequestParam("bikeId") String bikeId, HttpSession session) {
        Map<String, String> response = new HashMap<>();
        String userEmail = (String) session.getAttribute("userEmail");
        
        //authentication chck
        if (userEmail == null) {
            response.put("status", "error");
            response.put("message", "You must be logged in to return a bike.");
            return response;
        }
        //state update:Change the bike's availability back to 'true' in bikes.txt
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
