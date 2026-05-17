package com.example.bikerental.service;

import com.example.bikerental.model.Bike;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class BikeService {

    
    private static final String FILE_PATH = "bikes.txt";

    
    public BikeService() {
        File file = new File(FILE_PATH);
        try {
            
            
            if (!file.exists()) {
                file.createNewFile();
                addBike(new Bike("BK-101", "Yamaha", "MT-15", 7000.0, true, "Sport", "Colombo", "A stylish sport bike with strong performance, smooth handling, and good fuel efficiency.", "/images/mt15.webp"));
                addBike(new Bike("BK-102", "Honda", "Dio", 2500.0, true, "Scooter", "Kandy", "Easy and comfortable scooter for city rides and daily travel.", "/images/Honda-Dio-Black.png"));
                addBike(new Bike("BK-103", "Bajaj", "Pulsar", 4000.0, true, "Street", "Galle", "Reliable bike for daily and long rides with balanced comfort.", "/images/bajajpulsar.webp"));
                addBike(new Bike("BK-104", "Suzuki", "Gixxer", 6500.0, false, "Sport", "Colombo", "Modern sporty bike with good control and road presence.", "/images/GSX150RF-8GRAY.png"));
                addBike(new Bike("BK-105", "TVS", "Ntorq", 3000.0, true, "Scooter", "Negombo", "Premium scooter with comfort, storage, and easy riding controls.", "/images/ntorq.png"));
                addBike(new Bike("BK-106", "Honda", "CB150R", 7500.0, true, "Sport", "Colombo", "A premium naked sport bike with advanced features and smooth handling.", "/images/HONDA_CB_150_R.png"));
            }
        } catch (IOException e) {
            
            e.printStackTrace();
        }
    }

    
    public void addBike(Bike bike) {
        
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(bike.getId() + "," + bike.getBrand() + "," + bike.getModel() + "," + bike.getHourlyRate() + "," + bike.isAvailable() + "," + bike.getType() + "," + bike.getLocation() + "," + bike.getDescription() + "," + bike.getImageUrl());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    public List<Bike> getAllBikes() {
        List<Bike> bikes = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            
            while ((line = reader.readLine()) != null) {
                
                String[] details = line.split(",");
                if (details.length >= 9) {
                    bikes.add(new Bike(details[0], details[1], details[2], Double.parseDouble(details[3]), Boolean.parseBoolean(details[4]), details[5], details[6], details[7], details[8]));
                } else if (details.length == 8) {
                    
                    bikes.add(new Bike(details[0], details[1], details[2], Double.parseDouble(details[3]), Boolean.parseBoolean(details[4]), details[5], details[6], details[7], "/images/mt15.webp"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bikes; 
    }
    
    
    public Bike getBikeById(String id) {
        for(Bike b : getAllBikes()) {
            if(b.getId().equals(id)) return b;
        }
        return null; 
    }

    
    public boolean updateBike(Bike updatedBike) {
        List<Bike> bikes = getAllBikes();
        boolean found = false;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            for (Bike bike : bikes) {
                if (bike.getId().equals(updatedBike.getId())) {
                    bike.setBrand(updatedBike.getBrand());
                    bike.setModel(updatedBike.getModel());
                    bike.setHourlyRate(updatedBike.getHourlyRate());
                    bike.setType(updatedBike.getType());
                    bike.setLocation(updatedBike.getLocation());
                    bike.setDescription(updatedBike.getDescription());
                    found = true;
                }
                writer.write(bike.getId() + "," + bike.getBrand() + "," + bike.getModel() + "," + bike.getHourlyRate() + "," + bike.isAvailable() + "," + bike.getType() + "," + bike.getLocation() + "," + bike.getDescription() + "," + bike.getImageUrl());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return found;
    }

    
    public boolean updateBikeAvailability(String id, boolean available) {
        List<Bike> bikes = getAllBikes();
        boolean found = false;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            for (Bike bike : bikes) {
                if (bike.getId().equals(id)) {
                    bike.setAvailable(available);
                    found = true;
                }
                writer.write(bike.getId() + "," + bike.getBrand() + "," + bike.getModel() + "," + bike.getHourlyRate() + "," + bike.isAvailable() + "," + bike.getType() + "," + bike.getLocation() + "," + bike.getDescription() + "," + bike.getImageUrl());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return found;
    }

    
    public boolean deleteBike(String id) {
        List<Bike> bikes = getAllBikes();
        int before = bikes.size();
        bikes.removeIf(b -> b.getId().equals(id));
        if (bikes.size() == before) return false; 
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            for (Bike bike : bikes) {
                writer.write(bike.getId() + "," + bike.getBrand() + "," + bike.getModel() + "," + bike.getHourlyRate() + "," + bike.isAvailable() + "," + bike.getType() + "," + bike.getLocation() + "," + bike.getDescription() + "," + bike.getImageUrl());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
