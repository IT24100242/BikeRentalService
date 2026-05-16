package com.example.bikerental.service;

import com.example.bikerental.model.Rental;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class RentalService {

    // Constant representing our flat-file database for storing rental transactions.
    private static final String FILE_PATH = "rentals.txt";

    // Constructor: Automatically executed when the Spring application initializes.
    public RentalService() {
        File file = new File(FILE_PATH);
        try {
            if (!file.exists()) file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * CREATE Operation: Registers a new rental by appending a CSV line to the text file.
     */
    public synchronized void createRental(Rental rental) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            // Write using the full toFileString() so all fields are preserved.
            writer.write(rental.toFileString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Alias for createRental — used by RentalFileService and RentalViewController. */
    public synchronized void addRental(Rental rental) {
        createRental(rental);
    }

    /**
     * READ (All) Operation: Retrieves all rentals using the flexible fromFileString parser.
     */
    public synchronized List<Rental> getAllRentals() {
        List<Rental> rentals = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                try {
                    Rental rental = Rental.fromFileString(line);
                    if (rental != null) rentals.add(rental);
                } catch (Exception ignored) {
                    // Skip malformed lines safely
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rentals;
    }

    /**
     * READ (Filtered) Operation: Returns rentals belonging to a specific user.
     */
    public synchronized List<Rental> getRentalsByUserEmail(String userEmail) {
        List<Rental> result = new ArrayList<>();
        for (Rental r : getAllRentals()) {
            if (r.getUserEmail() != null && r.getUserEmail().equalsIgnoreCase(userEmail)) {
                result.add(r);
            }
        }
        return result;
    }

    /**
     * Generates a sequential rental ID like R001, R002, etc.
     */
    public synchronized String generateRentalId() {
        return String.format("R%03d", getAllRentals().size() + 1);
    }
}
