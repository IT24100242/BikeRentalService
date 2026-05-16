package com.example.bikerental.service;

import com.example.bikerental.model.Payment;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentService {

    private static final String FILE_PATH = "payments.txt";

    public PaymentService() {
        File file = new File(FILE_PATH);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processPayment(Payment payment) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(payment.getPaymentId() + "," + payment.getRentalId() + "," + payment.getAmount() + "," + payment.getMethod() + "," + payment.getStatus());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Payment> getAllPayments() {
        List<Payment> payments = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length >= 5) {
                    payments.add(new Payment(details[0], details[1], Double.parseDouble(details[2]), details[3], details[4]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return payments;
    }

    
    public java.util.Map<String, Double> getRevenueByDate() {
        java.util.Map<String, Double> map = new java.util.LinkedHashMap<>();
        List<Payment> payments = getAllPayments();
        
        
        for (int i = 0; i < payments.size(); i++) {
            String label = "Txn " + (i + 1);
            map.put(label, payments.get(i).getAmount());
        }
        return map;
    }

    
    public synchronized boolean deletePayment(String paymentId) {
        List<Payment> payments = getAllPayments();
        int before = payments.size();
        payments.removeIf(p -> p.getPaymentId().equalsIgnoreCase(paymentId));
        if (payments.size() == before) return false; 

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            for (Payment p : payments) {
                writer.write(p.getPaymentId() + "," + p.getRentalId() + ","
                        + p.getAmount() + "," + p.getMethod() + "," + p.getStatus());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    
    public java.util.Map<String, Double> getPaymentMethodBreakdown() {
        java.util.Map<String, Double> map = new java.util.LinkedHashMap<>();
        for (Payment p : getAllPayments()) {
            String method = p.getMethod() != null ? p.getMethod() : "Unknown";
            map.merge(method, p.getAmount(), (a, b) -> a + b);
        }
        return map;
    }
}
