package com.example.bikerental.model;

public class Rental {
    private String rentalId;
    private String username;
    private String userEmail;
    private String customerName;
    private String bikeId;
    private String bikeName;
    private String pickupLocation;
    private String dropLocation;
    private String rentalDate;
    private String startDate;
    private String endDate;
    private int rentalDays;
    private double pricePerDay;
    private double totalCost;
    private String status;

    public Rental() {
    }


    // Compatibility constructor used by existing RentalController and PaymentController.
    public Rental(String rentalId, String userEmail, String bikeId, String startDate, String endDate,
                  double totalCost, String status) {
        this.rentalId = rentalId;
        this.username = userEmail;
        this.userEmail = userEmail;
        this.customerName = userEmail;
        this.bikeId = bikeId;
        this.bikeName = bikeId;
        this.pickupLocation = "";
        this.dropLocation = "";
        this.rentalDate = startDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rentalDays = 1;
        this.pricePerDay = totalCost;
        this.totalCost = totalCost;
        this.status = status;
    }

    public Rental(String rentalId, String username, String customerName, String bikeName,
                  String pickupLocation, String dropLocation, String rentalDate,
                  int rentalDays, double pricePerDay, String status) {
        this.rentalId = rentalId;
        this.username = username;
        this.userEmail = username;
        this.customerName = customerName;
        this.bikeName = bikeName;
        this.bikeId = bikeName;
        this.pickupLocation = pickupLocation;
        this.dropLocation = dropLocation;
        this.rentalDate = rentalDate;
        this.startDate = rentalDate;
        this.endDate = rentalDate;
        this.rentalDays = rentalDays;
        this.pricePerDay = pricePerDay;
        this.totalCost = rentalDays * pricePerDay;
        this.status = status;
    }

    public Rental(String userEmail, String bikeId, String startDate, String endDate,
                  double totalCost, String status) {
        this.rentalId = "";
        this.username = userEmail;
        this.userEmail = userEmail;
        this.customerName = userEmail;
        this.bikeId = bikeId;
        this.bikeName = bikeId;
        this.pickupLocation = "";
        this.dropLocation = "";
        this.rentalDate = startDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rentalDays = 1;
        this.pricePerDay = totalCost;
        this.totalCost = totalCost;
        this.status = status;
    }

    public String getRentalId() { return rentalId; }
    public void setRentalId(String rentalId) { this.rentalId = rentalId; }

    public String getRentalID() { return rentalId; }
    public void setRentalID(String rentalId) { this.rentalId = rentalId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; this.userEmail = username; }

    public String getUserEmail() { return userEmail != null ? userEmail : username; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; this.username = userEmail; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getBikeId() { return bikeId != null ? bikeId : bikeName; }
    public void setBikeId(String bikeId) { this.bikeId = bikeId; this.bikeName = bikeId; }

    public String getBikeName() { return bikeName != null ? bikeName : bikeId; }
    public void setBikeName(String bikeName) { this.bikeName = bikeName; this.bikeId = bikeName; }

    public String getPickupLocation() { return pickupLocation; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }

    public String getDropLocation() { return dropLocation; }
    public void setDropLocation(String dropLocation) { this.dropLocation = dropLocation; }

    public String getRentalDate() { return rentalDate != null ? rentalDate : startDate; }
    public void setRentalDate(String rentalDate) { this.rentalDate = rentalDate; this.startDate = rentalDate; }

    public String getStartDate() { return startDate != null ? startDate : rentalDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; this.rentalDate = startDate; }

    public String getEndDate() { return endDate != null ? endDate : rentalDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public int getRentalDays() { return rentalDays; }
    public void setRentalDays(int rentalDays) { this.rentalDays = rentalDays; this.totalCost = rentalDays * pricePerDay; }

    public double getPricePerDay() { return pricePerDay; }
    public void setPricePerDay(double pricePerDay) { this.pricePerDay = pricePerDay; this.totalCost = rentalDays * pricePerDay; }

    public double getTotalAmount() { return totalCost > 0 ? totalCost : rentalDays * pricePerDay; }
    public double getTotalCost() { return getTotalAmount(); }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String toFileString() {
        return safe(rentalId) + "," + safe(username) + "," + safe(customerName) + "," + safe(getBikeName()) + ","
                + safe(pickupLocation) + "," + safe(dropLocation) + "," + safe(getRentalDate()) + ","
                + rentalDays + "," + pricePerDay + "," + safe(status);
    }

    public static Rental fromFileString(String line) {
        String[] data = line.split(",", -1);

        if (data.length == 7) {
            return new Rental(data[0], data[1], data[2], data[3], data[4],
                    Double.parseDouble(data[5]), data[6]);
        }

        if (data.length == 10) {
            return new Rental(data[0], data[1], data[2], data[3], data[4],
                    data[5], data[6], Integer.parseInt(data[7]),
                    Double.parseDouble(data[8]), data[9]);
        }

        if (data.length == 6) {
            return new Rental(data[0], data[1], data[2], data[3],
                    Double.parseDouble(data[4]), data[5]);
        }

        if (data.length == 5) {
            return new Rental(data[0], "user", data[1], data[2], "Colombo",
                    "Colombo", "2026-05-11", Integer.parseInt(data[3]),
                    Double.parseDouble(data[4]), "Confirmed");
        }

        return null;
    }

    private static String safe(String value) {
        return value == null ? "" : value;
    }
}
