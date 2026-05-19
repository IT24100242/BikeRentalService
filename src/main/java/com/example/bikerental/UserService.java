package com.example.bikerental.service;

import com.example.bikerental.model.User;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    
    private static final String FILE_PATH = "users.txt";
    private static final String ADMIN_FILE_PATH = "admins.txt";

    
    public UserService() {
        File file = new File(FILE_PATH);
        File adminFile = new File(ADMIN_FILE_PATH);
        try {
            
            if (!file.exists()) {
                file.createNewFile();
            }
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

    
    public boolean registerUser(User user) {
        
        if (searchUser(user.getEmail()) != null) {
            return false; 
        }
        
        
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            
            writer.write(user.getName() + "," + user.getEmail() + "," + user.getPassword());
            writer.newLine(); 
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    
    public User searchUser(String email) {
        
        User user = searchFileForUser(FILE_PATH, email);
        if (user != null) {
            return user;
        }
        
        return searchFileForUser("admins.txt", email);
    }

    
    private User searchFileForUser(String filePath, String email) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length >= 3) {
                    if (details[1].equals(email)) {
                        return new User(details[0], details[1], details[2]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    
    public boolean deleteUser(String email) {
        
        List<String> remainingUsers = new ArrayList<>();
        boolean deleted = false;

        
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                
                if (details.length >= 3 && details[1].equals(email)) {
                    deleted = true; 
                } else {
                    
                    remainingUsers.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        
        if (deleted) {
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
                for (String userLine : remainingUsers) {
                    writer.write(userLine);
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        return deleted;
    }

    
    public boolean validateLogin(String inputEmail, String inputPassword) {
        
        User user = searchUser(inputEmail);
        
        return user != null && user.getPassword().equals(inputPassword);
    }
    
    
    public List<User> getAllUsers() {
        
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length >= 3) {
                    
                    users.add(new User(details[0], details[1], details[2]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }
}