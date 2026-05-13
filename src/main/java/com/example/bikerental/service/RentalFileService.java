package com.example.bikerental.service;

import com.example.bikerental.model.Rental;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class RentalFileService {
    private static final String FILE_PATH = "data/rentals.txt";

    public RentalFileService() {
        createFileIfNotExists();
    }

    private void createFileIfNotExists() {
        try {
            File file = new File(FILE_PATH);
            File parent = file.getParentFile();

            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            if (!file.exists()) {
                file.createNewFile();
                addRental(new Rental("R001", "user", "Kamal Perera", "Yamaha MT-15",
                        "Colombo", "Gampaha", "2026-05-11", 3, 7000.0, "Confirmed"));
            }
        } catch (IOException e) {
            System.out.println("Error creating rental file: " + e.getMessage());
        }
    }

    public synchronized List<Rental> getAllRentals() {
        List<Rental> rentals = new ArrayList<>();
        createFileIfNotExists();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                try {
                    Rental rental = Rental.fromFileString(line);
                    if (rental != null) rentals.add(rental);
                } catch (Exception ignored) {
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading rentals: " + e.getMessage());
        }

        return rentals;
    }

    public synchronized List<Rental> getRentalsByUsername(String username) {
        List<Rental> userRentals = new ArrayList<>();

        for (Rental rental : getAllRentals()) {
            if (rental.getUsername() != null && rental.getUsername().equalsIgnoreCase(username)) {
                userRentals.add(rental);
            }
        }

        return userRentals;
    }

    public synchronized void addRental(Rental rental) {
        createFileIfNotExists();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            bw.write(rental.toFileString());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error adding rental: " + e.getMessage());
        }
    }

    public synchronized String generateRentalId() {
        int next = getAllRentals().size() + 1;
        return String.format("R%03d", next);
    }
}
