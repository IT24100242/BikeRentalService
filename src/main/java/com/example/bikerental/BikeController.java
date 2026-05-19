package com.example.bikerental.controller;

import com.example.bikerental.model.Bike;
import com.example.bikerental.service.BikeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class BikeController {

    
    private static final java.util.Set<String> PROTECTED_IDS =
        new java.util.HashSet<>(java.util.Arrays.asList("BK-101","BK-102","BK-103","BK-104","BK-105"));

    @Autowired
    private BikeService bikeService;

    
    @GetMapping("/api/bikes")
    public List<Bike> getAllBikes() {
        return bikeService.getAllBikes();
    }

    
    @PostMapping("/api/bikes/add")
    public Map<String, String> addBike(
            @RequestParam("brand")       String brand,
            @RequestParam("model")       String model,
            @RequestParam("type")        String type,
            @RequestParam("hourlyRate")  double hourlyRate,
            @RequestParam("location")    String location,
            @RequestParam("description") String description,
            @RequestParam("imageUrl")    String imageUrl,
            HttpSession session) {

        Map<String, String> response = new HashMap<>();
        if (session.getAttribute("userEmail") == null) {
            response.put("status", "error");
            response.put("message", "You must be logged in.");
            return response;
        }
        String id = "BK-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
        Bike newBike = new Bike(id, brand, model, hourlyRate, true, type, location, description, imageUrl);
        bikeService.addBike(newBike);
        response.put("status", "success");
        response.put("message", "Bike added successfully!");
        response.put("id", id);
        return response;
    }

    
    @PostMapping("/api/bikes/delete")
    public Map<String, String> deleteBike(
            @RequestParam("bikeId") String bikeId,
            HttpSession session) {

        Map<String, String> response = new HashMap<>();
        if (session.getAttribute("userEmail") == null) {
            response.put("status", "error");
            response.put("message", "You must be logged in.");
            return response;
        }
        
        boolean isAdmin = "admin@bikerental.com".equals(session.getAttribute("userEmail"));
        if (!isAdmin && PROTECTED_IDS.contains(bikeId)) {
            response.put("status", "error");
            response.put("message", "This bike is part of the original fleet and cannot be deleted.");
            return response;
        }
        boolean deleted = bikeService.deleteBike(bikeId);
        if (deleted) {
            response.put("status", "success");
            response.put("message", "Bike deleted.");
        } else {
            response.put("status", "error");
            response.put("message", "Bike not found.");
        }
        return response;
    }

    
    @PostMapping("/api/bikes/update")
    public Map<String, String> updateBike(
            @RequestParam("bikeId")      String bikeId,
            @RequestParam("brand")       String brand,
            @RequestParam("model")       String model,
            @RequestParam("type")        String type,
            @RequestParam("hourlyRate")  double hourlyRate,
            @RequestParam("location")    String location,
            @RequestParam("description") String description,
            @RequestParam("imageUrl")    String imageUrl,
            HttpSession session) {

        Map<String, String> response = new HashMap<>();
        String userEmail = (String) session.getAttribute("userEmail");
        if (userEmail == null || !"admin@bikerental.com".equals(userEmail)) {
            response.put("status", "error");
            response.put("message", "Only admins can edit bikes.");
            return response;
        }

        Bike existing = bikeService.getBikeById(bikeId);
        if (existing == null) {
            response.put("status", "error");
            response.put("message", "Bike not found.");
            return response;
        }

        Bike updatedBike = new Bike(bikeId, brand, model, hourlyRate, existing.isAvailable(), type, location, description, imageUrl);
        boolean updated = bikeService.updateBike(updatedBike);
        
        if (updated) {
            response.put("status", "success");
            response.put("message", "Bike updated successfully!");
        } else {
            response.put("status", "error");
            response.put("message", "Failed to update bike.");
        }
        return response;
    }
}
