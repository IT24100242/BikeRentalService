package com.example.bikerental.service;

import com.example.bikerental.model.Rental;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class RentalService {
    private static final String FILE_PATH = "rentals.txt";

    public RentalService() {
        createFileIfNotExists();
    }

    private void createFileIfNotExists() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void createRental(Rental rental) {
        createFileIfNotExists();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(rental.toFileString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void addRental(Rental rental) {
        createRental(rental);
    }

    public synchronized List<Rental> getAllRentals() {
        createFileIfNotExists();
        List<Rental> rentals = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                try {
                    Rental rental = Rental.fromFileString(line);
                    if (rental != null) rentals.add(rental);
                } catch (Exception ignored) {
                    // Skip invalid lines safely.
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rentals;
    }

    public synchronized List<Rental> getRentalsByUserEmail(String userEmail) {
        List<Rental> userRentals = new ArrayList<>();
        for (Rental rental : getAllRentals()) {
            if (rental.getUserEmail() != null && rental.getUserEmail().equalsIgnoreCase(userEmail)) {
                userRentals.add(rental);
            }
        }
        return userRentals;
    }

    public synchronized String generateRentalId() {
        int next = getAllRentals().size() + 1;
        return String.format("R%03d", next);
    }
}
