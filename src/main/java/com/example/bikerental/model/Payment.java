package com.example.bikerental.model;

public class Payment {
    private String paymentId;
    private String rentalId;
    private double amount;
    private String method;
    private String status;

    public Payment() {}

    public Payment(String paymentId, String rentalId, double amount, String method, String status) {
        this.paymentId = paymentId;
        this.rentalId = rentalId;
        this.amount = amount;
        this.method = method;
        this.status = status;
    }

    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }
    public String getRentalId() { return rentalId; }
    public void setRentalId(String rentalId) { this.rentalId = rentalId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
