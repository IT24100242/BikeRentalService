package com.example.bikerental.service;

import com.example.bikerental.model.Admin;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminManagement {

    private static final String ADMIN_FILE_PATH = "admins.txt";

    public AdminManagement() {
        File adminFile = new File(ADMIN_FILE_PATH);
        try {
            if (!adminFile.exists()) {
                adminFile.createNewFile();
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(adminFile))) {
                    writer.write("Admin,admin@bikerental.com,admin123");
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean registerAdmin(Admin admin) {
        if (searchAdmin(admin.getEmail()) != null) {
            return false;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ADMIN_FILE_PATH, true))) {
            String role = (admin.getRole() != null && !admin.getRole().isEmpty()) ? admin.getRole() : "System Admin";
            writer.write(admin.getName() + "," + admin.getEmail() + "," + admin.getPassword() + "," + role);
            writer.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Admin> getAllAdmins() {
        List<Admin> admins = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ADMIN_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length >= 3) {
                    String adminId = "ADM-" + Integer.toHexString(details[1].hashCode()).toUpperCase();
                    String role = details.length >= 4 ? details[3] : "System Admin";
                    admins.add(new Admin(adminId, details[0], details[1], details[2], role));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return admins;
    }

    public Admin searchAdmin(String email) {
        try (BufferedReader reader = new BufferedReader(new FileReader(ADMIN_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length >= 3 && details[1].equalsIgnoreCase(email)) {
                    String adminId = "ADM-" + Integer.toHexString(details[1].hashCode()).toUpperCase();
                    String role = details.length >= 4 ? details[3] : "System Admin";
                    return new Admin(adminId, details[0], details[1], details[2], role);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateAdmin(String oldEmail, Admin updatedAdmin) {
        // Prevent changing primary admin email to prevent lockout
        if ("admin@bikerental.com".equals(oldEmail) && !"admin@bikerental.com".equals(updatedAdmin.getEmail())) {
            return false;
        }

        List<Admin> allAdmins = getAllAdmins();
        boolean updated = false;

        // Check for email collision (if email was changed, ensure it's not taken by another admin)
        for (Admin adm : allAdmins) {
            if (!adm.getEmail().equalsIgnoreCase(oldEmail) && adm.getEmail().equalsIgnoreCase(updatedAdmin.getEmail())) {
                return false; // Email is already in use by another administrator
            }
        }

        for (Admin adm : allAdmins) {
            if (adm.getEmail().equalsIgnoreCase(oldEmail)) {
                adm.setName(updatedAdmin.getName());
                adm.setEmail(updatedAdmin.getEmail());
                adm.setPassword(updatedAdmin.getPassword());
                
                // Primary admin must remain System Admin
                if ("admin@bikerental.com".equals(adm.getEmail())) {
                    adm.setRole("System Admin");
                } else {
                    adm.setRole(updatedAdmin.getRole());
                }
                
                updated = true;
                break;
            }
        }

        if (updated) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(ADMIN_FILE_PATH, false))) {
                for (Admin adm : allAdmins) {
                    writer.write(adm.getName() + "," + adm.getEmail() + "," + adm.getPassword() + "," + adm.getRole());
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        return updated;
    }

    public boolean deleteAdmin(String email) {
        if ("admin@bikerental.com".equals(email)) {
            return false;
        }
        List<String> remainingAdmins = new ArrayList<>();
        boolean deleted = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(ADMIN_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length >= 3 && details[1].equals(email)) {
                    deleted = true;
                } else {
                    remainingAdmins.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (deleted) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(ADMIN_FILE_PATH, false))) {
                for (String adminLine : remainingAdmins) {
                    writer.write(adminLine);
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return deleted;
    }
}
