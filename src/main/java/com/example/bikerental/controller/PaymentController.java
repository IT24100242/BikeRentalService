package com.example.bikerental.controller;

import com.example.bikerental.model.Payment;
import com.example.bikerental.model.Rental;
import com.example.bikerental.service.BikeService;
import com.example.bikerental.service.PaymentService;
import com.example.bikerental.service.RentalService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
public class PaymentController {

    @Autowired private PaymentService paymentService;
    @Autowired private RentalService  rentalService;
    @Autowired private BikeService    bikeService;

    
    @GetMapping("/payment")
    public String showPaymentPage(@RequestParam("bikeId")    String bikeId,
                                  @RequestParam("bikeName")  String bikeName,
                                  @RequestParam("startDate") String startDate,
                                  @RequestParam("endDate")   String endDate,
                                  @RequestParam("cost")      double cost,
                                  Model model) {
        model.addAttribute("bikeId",    bikeId);
        model.addAttribute("bikeName",  bikeName);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate",   endDate);
        model.addAttribute("cost",      cost);
        return "payment";
    }

    
    @PostMapping("/pay")
    public String processPayment(@RequestParam("bikeId")    String bikeId,
                                 @RequestParam("startDate") String startDate,
                                 @RequestParam("endDate")   String endDate,
                                 @RequestParam("cost")      double cost,
                                 @RequestParam("method")    String method,
                                 HttpSession session) {

        String userEmail = (String) session.getAttribute("userEmail");
        if (userEmail == null) return "redirect:/login";

        
        bikeService.updateBikeAvailability(bikeId, false);

        
        String rentalId = "R-" + UUID.randomUUID().toString().substring(0, 5);
        Rental rental = new Rental(rentalId, userEmail, bikeId, startDate, endDate, cost, "Pending");
        rentalService.createRental(rental);

        
        String paymentId = "PAY-" + UUID.randomUUID().toString().substring(0, 5);
        Payment payment = new Payment(paymentId, rentalId, cost, method, "Completed");
        paymentService.processPayment(payment);

        return "redirect:/bikes?paid=true";
    }

    
    @PostMapping("/admin/payments/delete")
    public String deletePayment(@RequestParam("paymentId") String paymentId) {
        paymentService.deletePayment(paymentId);
        return "redirect:/admin?tab=payments&deletedPayment=true";
    }
}
