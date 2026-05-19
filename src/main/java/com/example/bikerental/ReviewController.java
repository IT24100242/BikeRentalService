package com.example.bikerental.controller;

import com.example.bikerental.model.Review;
import com.example.bikerental.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.UUID;

@Controller
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/reviews")
    public String showReviews(Model model) {
        model.addAttribute("reviews", reviewService.getAllReviews());
        return "reviews";
    }

    
    @GetMapping("/api/reviews")
    @ResponseBody
    public List<Review> getAllReviewsJson() {
        return reviewService.getAllReviews();
    }

    @PostMapping("/review")
    public String addReview(@RequestParam("userEmail") String userEmail,
                            @RequestParam("bikeId") String bikeId,
                            @RequestParam("rating") int rating,
                            @RequestParam("comment") String comment,
                            HttpSession session) {
        
        String email = (userEmail != null && !userEmail.isEmpty())
            ? userEmail
            : (String) session.getAttribute("userEmail");
        String reviewId = "REV-" + UUID.randomUUID().toString().substring(0, 5);
        Review review = new Review(reviewId, email, bikeId, rating, comment);
        reviewService.addReview(review);
        return "redirect:/bikes?reviewed=true";
    }

    
    @PostMapping("/admin/reviews/delete")
    public String deleteReview(@RequestParam("reviewId") String reviewId) {
        reviewService.deleteReview(reviewId);
        return "redirect:/admin?tab=reviews";
    }
}
