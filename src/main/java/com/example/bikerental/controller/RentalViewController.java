package com.example.bikerental.controller;

// this controller handles the rentals page for users.it is for rental management ui, not api.
import com.example.bikerental.model.Rental;
import com.example.bikerental.service.RentalFileService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
public class RentalViewController {

    private final RentalFileService rentalFileService;

    public RentalViewController(RentalFileService rentalFileService) {
        this.rentalFileService = rentalFileService;
    }

    @GetMapping("/rentals")
    public String showUserRentals(Model model, Principal principal, HttpSession session) {
        String username = getLoggedUser(principal, session);
        List<Rental> rentals = rentalFileService.getRentalsByUsername(username);
        addRentalAttributes(model, username, false, rentals);
        return "rentals";
    }

    @PostMapping("/rentals")
    public String createRental(@RequestParam String bikeName,
                               @RequestParam String pickupLocation,
                               @RequestParam String dropLocation,
                               @RequestParam String rentalDate,
                               @RequestParam int rentalDays,
                               @RequestParam double pricePerDay,
                               Principal principal,
                               HttpSession session) {
        String username = getLoggedUser(principal, session);
        Rental rental = new Rental(
                rentalFileService.generateRentalId(),
                username,
                username,   // customerName not collected — use username
                bikeName,
                pickupLocation,
                dropLocation,
                rentalDate,
                rentalDays,
                pricePerDay,
                "Pending"
        );
        rentalFileService.addRental(rental);
        return "redirect:/rentals";
    }

    @GetMapping("/admin/rentals")
    public String showAdminRentals(Model model) {
        List<Rental> rentals = rentalFileService.getAllRentals();
        addRentalAttributes(model, "admin", true, rentals);
        return "rentals";
    }

    @GetMapping("/my-rentals")
    public String redirectMyRentals() {
        return "redirect:/rentals";
    }

    //admin can approve or reject a pending rental |action can be confirm or reject.

    @PostMapping("/admin/rentals/approve")
    public String approveOrRejectRental(@RequestParam String rentalId,
                                        @RequestParam String action) {
        rentalFileService.updateRentalStatus(rentalId, action);
        return "redirect:/admin?tab=rentals";
    }

    private void addRentalAttributes(Model model, String username, boolean admin, List<Rental> rentals) {
        double totalAmount = rentals.stream().mapToDouble(Rental::getTotalAmount).sum();
        model.addAttribute("rentals", rentals);
        model.addAttribute("username", username);
        model.addAttribute("admin", admin);
        model.addAttribute("rentalCount", rentals.size());
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("today", LocalDate.now().toString());
    }

    private String getLoggedUser(Principal principal, HttpSession session) {
        if (principal != null && principal.getName() != null) return principal.getName();
        Object sessionEmail = session.getAttribute("userEmail");
        if (sessionEmail != null) return sessionEmail.toString();
        return "user";
    }
}
