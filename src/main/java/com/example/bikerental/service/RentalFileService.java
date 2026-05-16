package com.example.bikerental.service;

import com.example.bikerental.model.Rental;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


@Service
public class RentalFileService {

    
    private static final String FILE_PATH = "rentals.txt";

    public RentalFileService() {
        createFileIfNotExists();
    }

    private void createFileIfNotExists() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) file.createNewFile();
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
                    Rental r = Rental.fromFileString(line);
                    if (r != null) rentals.add(r);
                } catch (Exception ignored) {}
            }
        } catch (IOException e) {
            System.out.println("Error reading rentals: " + e.getMessage());
        }
        return rentals;
    }

    public synchronized List<Rental> getRentalsByUsername(String username) {
        List<Rental> result = new ArrayList<>();
        for (Rental r : getAllRentals()) {
            if (r.getUsername() != null && r.getUsername().equalsIgnoreCase(username)) {
                result.add(r);
            }
        }
        return result;
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

    
    public synchronized boolean updateRentalStatus(String rentalId, String newStatus) {
        List<Rental> rentals = getAllRentals();
        boolean found = false;

        for (Rental rental : rentals) {
            if (rental.getRentalId() != null && rental.getRentalId().equalsIgnoreCase(rentalId)) {
                rental.setStatus(newStatus);
                found = true;
                break;
            }
        }

        if (found) {
            
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
                for (Rental r : rentals) {
                    bw.write(r.toFileString());
                    bw.newLine();
                }
            } catch (IOException e) {
                System.out.println("Error updating rental status: " + e.getMessage());
                return false;
            }
        }
        return found;
    }
}
