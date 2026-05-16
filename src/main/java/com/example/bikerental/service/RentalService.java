package com.example.bikerental.service;

import com.example.bikerental.model.Rental;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class RentalService {

    // this file is used to store rental details.
    private static final String FILE_PATH = "rentals.txt";

    // constructor--->this runs when the application starts.
    public RentalService() {
        File file = new File(FILE_PATH);
        try {
            if (!file.exists()) file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //create operation-->this saves a new rental to the text file.

    public synchronized void createRental(Rental rental) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            // save full rental details as one line.
            writer.write(rental.toFileString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // this method also saves a rental.
    public synchronized void addRental(Rental rental) {
        createRental(rental);
    }

    // this reads all rental details from the file.
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

    //read {filter}ope: Returns rentals belonging to a specific user.
    public synchronized List<Rental> getRentalsByUserEmail(String userEmail) {
        List<Rental> result = new ArrayList<>();
        for (Rental r : getAllRentals()) {
            if (r.getUserEmail() != null && r.getUserEmail().equalsIgnoreCase(userEmail)) {
                result.add(r);
            }
        }
        return result;
    }

//create rentalID
    public synchronized String generateRentalId() {
        return String.format("R%03d", getAllRentals().size() + 1);
    }
}
