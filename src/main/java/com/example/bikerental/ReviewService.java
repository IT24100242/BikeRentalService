package com.example.bikerental.service;

import com.example.bikerental.model.Review;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewService {

    private static final String FILE_PATH = "reviews.txt";

    public ReviewService() {
        File file = new File(FILE_PATH);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addReview(Review review) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(review.getReviewId() + "," + review.getUserEmail() + "," + review.getBikeId() + "," + review.getRating() + "," + review.getComment());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Review> getAllReviews() {
        List<Review> reviews = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",", 5); 
                if (details.length >= 5) {
                    reviews.add(new Review(details[0], details[1], details[2], Integer.parseInt(details[3]), details[4]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    
    public synchronized boolean deleteReview(String reviewId) {
        List<Review> reviews = getAllReviews();
        int before = reviews.size();
        reviews.removeIf(r -> r.getReviewId().equalsIgnoreCase(reviewId));
        if (reviews.size() == before) return false; 

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            for (Review r : reviews) {
                writer.write(r.getReviewId() + "," + r.getUserEmail() + "," + r.getBikeId() + "," + r.getRating() + "," + r.getComment());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
