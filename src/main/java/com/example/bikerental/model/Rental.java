package com.example.bikerental.model;

//This model class represents a Bike Rental transaction and supports both the old 7-field format and new 10-field format.
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

    public Rental() {}

    // old constructor used by rentalcontroller and paymentcontroller
    public Rental(String rentalId, String userEmail, String bikeId, String startDate, String endDate,
                  double totalCost, String status) {
        this.rentalId     = rentalId;
        this.username     = userEmail;
        this.userEmail    = userEmail;
        this.customerName = userEmail;
        this.bikeId       = bikeId;
        this.bikeName     = bikeId;
        this.pickupLocation = "";
        this.dropLocation   = "";
        this.rentalDate   = startDate;
        this.startDate    = startDate;
        this.endDate      = endDate;
        this.rentalDays   = 1;
        this.pricePerDay  = totalCost;
        this.totalCost    = totalCost;
        this.status       = status;
    }

    // extended constructor used by rentalviewcontroller
    public Rental(String rentalId, String username, String customerName, String bikeName,
                  String pickupLocation, String dropLocation, String rentalDate,
                  int rentalDays, double pricePerDay, String status) {
        this.rentalId       = rentalId;
        this.username       = username;
        this.userEmail      = username;
        this.customerName   = customerName;
        this.bikeName       = bikeName;
        this.bikeId         = bikeName;
        this.pickupLocation = pickupLocation;
        this.dropLocation   = dropLocation;
        this.rentalDate     = rentalDate;
        this.startDate      = rentalDate;
        this.endDate        = rentalDate;
        this.rentalDays     = rentalDays;
        this.pricePerDay    = pricePerDay;
        this.totalCost      = rentalDays * pricePerDay;
        this.status         = status;
    }

    // compact constructor without rentalid
    public Rental(String userEmail, String bikeId, String startDate, String endDate,
                  double totalCost, String status) {
        this("", userEmail, bikeId, startDate, endDate, totalCost, status);
    }

    //getters and setters

    public String getRentalId()               { return rentalId; }
    public void   setRentalId(String rentalId){ this.rentalId = rentalId; }

    // kept this alias so old code still works
    public String getRentalID()               { return rentalId; }
    public void   setRentalID(String id)      { this.rentalId = id; }

    public String getUsername()               { return username; }
    public void   setUsername(String u)       { this.username = u; this.userEmail = u; }

    public String getUserEmail()              { return userEmail != null ? userEmail : username; }
    public void   setUserEmail(String e)      { this.userEmail = e; this.username = e; }

    public String getCustomerName()           { return customerName; }
    public void   setCustomerName(String c)   { this.customerName = c; }

    public String getBikeId()                 { return bikeId != null ? bikeId : bikeName; }
    public void   setBikeId(String id)        { this.bikeId = id; this.bikeName = id; }

    public String getBikeName()               { return bikeName != null ? bikeName : bikeId; }
    public void   setBikeName(String n)       { this.bikeName = n; this.bikeId = n; }

    public String getPickupLocation()         { return pickupLocation; }
    public void   setPickupLocation(String l) { this.pickupLocation = l; }

    public String getDropLocation()           { return dropLocation; }
    public void   setDropLocation(String l)   { this.dropLocation = l; }

    public String getRentalDate()             { return rentalDate != null ? rentalDate : startDate; }
    public void   setRentalDate(String d)     { this.rentalDate = d; this.startDate = d; }

    public String getStartDate()              { return startDate != null ? startDate : rentalDate; }
    public void   setStartDate(String d)      { this.startDate = d; this.rentalDate = d; }

    public String getEndDate()                { return endDate != null ? endDate : rentalDate; }
    public void   setEndDate(String d)        { this.endDate = d; }

    public int    getRentalDays()             { return rentalDays; }
    public void   setRentalDays(int days)     { this.rentalDays = days; this.totalCost = days * pricePerDay; }

    public double getPricePerDay()            { return pricePerDay; }
    public void   setPricePerDay(double p)    { this.pricePerDay = p; this.totalCost = rentalDays * p; }

    public double getTotalAmount()            { return totalCost > 0 ? totalCost : rentalDays * pricePerDay; }
    public double getTotalCost()              { return getTotalAmount(); }
    public void   setTotalCost(double c)      { this.totalCost = c; }

    public String getStatus()                 { return status; }
    public void   setStatus(String s)         { this.status = s; }

    // used to convert rental data to text
    // converts rental details into a csv line for rentalfileservice
    public String toFileString() {
        return safe(rentalId) + "," + safe(username) + "," + safe(customerName) + "," + safe(getBikeName()) + ","
                + safe(pickupLocation) + "," + safe(dropLocation) + "," + safe(getRentalDate()) + ","
                + rentalDays + "," + pricePerDay + "," + safe(status);
    }
    // reads a csv line from rentals.txt and supports old and new formats
    public static Rental fromFileString(String line) {
        String[] d = line.split(",", -1);
        if (d.length == 10)
            return new Rental(d[0], d[1], d[2], d[3], d[4], d[5], d[6],
                    Integer.parseInt(d[7]), Double.parseDouble(d[8]), d[9]);
        if (d.length == 7)
            return new Rental(d[0], d[1], d[2], d[3], d[4], Double.parseDouble(d[5]), d[6]);
        if (d.length == 6)
            return new Rental(d[0], d[1], d[2], d[3], Double.parseDouble(d[4]), d[5]);
        return null;
    }

    private static String safe(String v) { return v == null ? "" : v; }
}
