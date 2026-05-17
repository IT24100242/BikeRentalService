package com.example.bikerental.model;

public class Bike {
    private String id;
    private String brand;
    private String model;
    private double hourlyRate;
    private boolean available;

    private String type;
    private String location;
    private String description;
    private String imageUrl;

    public Bike() {
    }

    public Bike(String id, String brand, String model, double hourlyRate, boolean available, String type,
            String location, String description, String imageUrl) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.hourlyRate = hourlyRate;
        this.available = available;
        this.type = type;
        this.location = location;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
