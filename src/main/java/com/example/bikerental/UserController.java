package com.example.bikerental.controller;

import com.example.bikerental.model.User;
import com.example.bikerental.model.Admin;
import com.example.bikerental.model.Bike;
import com.example.bikerental.model.Rental;
import com.example.bikerental.model.Payment;
import com.example.bikerental.service.UserService;
import com.example.bikerental.service.AdminManagement;
import com.example.bikerental.service.BikeService;
import com.example.bikerental.service.RentalFileService;
import com.example.bikerental.service.ReviewService;
import com.example.bikerental.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {

    
    
    
    @Autowired
    private UserService userService;

    @Autowired
    private AdminManagement adminManagement;

    @Autowired
    private BikeService bikeService;

    @Autowired
    private RentalFileService rentalFileService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private PaymentService paymentService;

    

    @GetMapping("/")
    public String showLandingPage() {
        return "index";
    }

    
    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    
    @PostMapping("/login")
    public String processLogin(@RequestParam("email") String email,
            @RequestParam("pass") String password,
            HttpSession session,
            Model model) {

        
        boolean isValid = userService.validateLogin(email, password);

        if (isValid) {
            
            
            session.setAttribute("userEmail", email);
            User user = userService.searchUser(email);
            if (user != null) {
                session.setAttribute("userName", user.getName());
            }

            
            
            boolean isAdmin = email.equals("admin@bikerental.com");
            String role = isAdmin ? "ROLE_ADMIN" : "ROLE_USER";
            String redirectUrl = isAdmin ? "redirect:/admin" : "redirect:/";

            
            
            
            org.springframework.security.authentication.UsernamePasswordAuthenticationToken auth = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                    email, null, java.util.Collections.singletonList(
                            new org.springframework.security.core.authority.SimpleGrantedAuthority(role)));
            org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(auth);

            
            session.setAttribute("SPRING_SECURITY_CONTEXT",
                    org.springframework.security.core.context.SecurityContextHolder.getContext());

            
            
            return redirectUrl;
        } else {
            
            
            model.addAttribute("error", "Invalid email or password");
            return "login"; 
        }
    }

    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); 
        return "redirect:/login";
    }

    
    @GetMapping("/api/session")
    @ResponseBody
    public Map<String, Object> getSession(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        String userEmail = (String) session.getAttribute("userEmail");
        String userName = (String) session.getAttribute("userName");
        if (userEmail != null) {
            response.put("loggedIn", true);
            response.put("email", userEmail);
            response.put("name", userName);
        } else {
            response.put("loggedIn", false);
        }
        return response;
    }

    
    
    

    @GetMapping("/register")
    public String showRegister() {
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("pass") String password,
            Model model) {
        
        User newUser = new User(name, email, password);
        
        boolean success = userService.registerUser(newUser);

        if (success) {
            return "redirect:/login?registered=true";
        } else {
            model.addAttribute("error", "User with this email already exists!");
            return "register";
        }
    }

    
    
    

    @GetMapping("/admin")
    public String showAdminDashboard(@RequestParam(value = "searchEmail", required = false) String searchEmail,
            Model model) {
        
        List<Bike> allBikes = bikeService.getAllBikes();
        int totalBikes = allBikes.size();
        int availableBikes = 0;
        for (Bike b : allBikes) {
            if (b.isAvailable()) {
                availableBikes++;
            }
        }
        int bookedBikes = totalBikes - availableBikes;

        model.addAttribute("totalBikes", totalBikes);
        model.addAttribute("availableBikes", availableBikes);
        model.addAttribute("bookedBikes", bookedBikes);

        
        List<Rental> allRentals = rentalFileService.getAllRentals();
        model.addAttribute("totalRentals", allRentals.size());
        model.addAttribute("allBikesList", allBikes);
        model.addAttribute("allRentals", allRentals);

        
        
        
        List<Payment> allPayments = paymentService.getAllPayments();
        double totalRevenue = allPayments.stream().mapToDouble(Payment::getAmount).sum();
        model.addAttribute("totalRevenue", String.format("%.0f", totalRevenue));

        
        java.util.Map<String, Double> bikeRevenue = new java.util.LinkedHashMap<>();
        for (Bike b : allBikes) {
            double rev = allRentals.stream()
                    .filter(r -> r.getBikeId() != null && r.getBikeId().equals(b.getId()))
                    .mapToDouble(Rental::getTotalCost).sum();
            bikeRevenue.put(b.getBrand() + " " + b.getModel(), rev);
        }
        model.addAttribute("bikeRevenueKeys", new java.util.ArrayList<>(bikeRevenue.keySet()));
        model.addAttribute("bikeRevenueVals", new java.util.ArrayList<>(bikeRevenue.values()));

        
        java.util.Map<String, Double> dailyRevenue = paymentService.getRevenueByDate();
        model.addAttribute("dailyRevenueKeys", new java.util.ArrayList<>(dailyRevenue.keySet()));
        model.addAttribute("dailyRevenueVals", new java.util.ArrayList<>(dailyRevenue.values()));

        java.util.Map<String, Double> methodBreakdown = paymentService.getPaymentMethodBreakdown();
        model.addAttribute("paymentMethodKeys", new java.util.ArrayList<>(methodBreakdown.keySet()));
        model.addAttribute("paymentMethodVals", new java.util.ArrayList<>(methodBreakdown.values()));

        model.addAttribute("totalPayments", allPayments.size());
        model.addAttribute("totalPaymentRevenue", String.format("%.0f", totalRevenue));
        model.addAttribute("allPayments", allPayments);

        
        int completedRentals = 0;
        java.util.Map<String, Integer> bikeCountMap = new java.util.HashMap<>();
        java.util.Map<String, Double> monthlyRevenueMap = new java.util.TreeMap<>(); 

        
        for (Bike b : allBikes) {
            bikeCountMap.put(b.getBrand() + " " + b.getModel(), 0);
        }

        for (Rental r : allRentals) {
            if ("Confirmed".equalsIgnoreCase(r.getStatus())) {
                completedRentals++;

                
                String date = r.getRentalDate();
                if (date != null && date.length() >= 7) {
                    String month = date.substring(0, 7); 
                    monthlyRevenueMap.put(month, monthlyRevenueMap.getOrDefault(month, 0.0) + r.getTotalCost());
                }
            }

            
            String bName = r.getBikeName();
            if (bName != null) {
                
                String displayName = bName;
                for (Bike b : allBikes) {
                    if (b.getId().equals(bName)) {
                        displayName = b.getBrand() + " " + b.getModel();
                        break;
                    }
                }
                bikeCountMap.put(displayName, bikeCountMap.getOrDefault(displayName, 0) + 1);
            }
        }

        model.addAttribute("completedRentals", completedRentals);

        
        String mostRented = "—";
        String leastRented = "—";
        int maxCount = -1;
        int minCount = Integer.MAX_VALUE;

        for (java.util.Map.Entry<String, Integer> entry : bikeCountMap.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostRented = entry.getKey() + " (" + maxCount + ")";
            }
            if (entry.getValue() < minCount && entry.getValue() >= 0) {
                minCount = entry.getValue();
                leastRented = entry.getKey() + " (" + minCount + ")";
            }
        }
        
        if (maxCount <= 0) {
            mostRented = "—";
            leastRented = "—";
        }

        model.addAttribute("mostRentedBike", mostRented);
        model.addAttribute("leastRentedBike", leastRented);

        
        model.addAttribute("monthlyRevenueKeys", new java.util.ArrayList<>(monthlyRevenueMap.keySet()));
        model.addAttribute("monthlyRevenueVals", new java.util.ArrayList<>(monthlyRevenueMap.values()));

        
        List<java.util.Map.Entry<String, Integer>> sortedBikeCounts = new java.util.ArrayList<>(
                bikeCountMap.entrySet());
        sortedBikeCounts.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        java.util.List<String> bNames = new java.util.ArrayList<>();
        java.util.List<Integer> bCounts = new java.util.ArrayList<>();
        for (java.util.Map.Entry<String, Integer> entry : sortedBikeCounts) {
            bNames.add(entry.getKey());
            bCounts.add(entry.getValue());
        }
        model.addAttribute("bikeCountKeys", bNames);
        model.addAttribute("bikeCountVals", bCounts);

        
        model.addAttribute("allReviews", reviewService.getAllReviews());

        
        if (searchEmail != null && !searchEmail.isEmpty()) {
            
            User user = userService.searchUser(searchEmail);
            if (user != null) {
                
                model.addAttribute("users", java.util.List.of(user));
            } else {
                model.addAttribute("error", "No user found with email: " + searchEmail);
            }
        } else {
            
            model.addAttribute("users", userService.getAllUsers());
        }

        
        model.addAttribute("allAdmins", adminManagement.getAllAdmins());

        return "admin"; 
    }

    @PostMapping("/admin/delete")
    public String deleteUser(@RequestParam("email") String email, Model model) {
        
        boolean deleted = userService.deleteUser(email);
        if (deleted) {
            return "redirect:/admin?deleted=true";
        } else {
            return "redirect:/admin?error=true";
        }
    }

    @PostMapping("/admin/users/add")
    public String addUser(@RequestParam("name") String name,
                          @RequestParam("email") String email,
                          @RequestParam("password") String password,
                          Model model) {
        User newUser = new User(name, email, password);
        boolean success = userService.registerUser(newUser);
        if (success) {
            return "redirect:/admin?tab=users&addedUser=true";
        } else {
            return "redirect:/admin?tab=users&errorUser=true";
        }
    }

    @PostMapping("/admin/admins/add")
    public String addAdmin(@RequestParam("name") String name,
                           @RequestParam("email") String email,
                           @RequestParam("password") String password,
                           @RequestParam("role") String role,
                           Model model) {
        Admin newAdmin = new Admin("ADM-" + java.util.UUID.randomUUID().toString().substring(0, 5), name, email, password, role);
        boolean success = adminManagement.registerAdmin(newAdmin);
        if (success) {
            return "redirect:/admin?tab=admins&addedAdmin=true";
        } else {
            return "redirect:/admin?tab=admins&errorAdmin=true";
        }
    }

    @PostMapping("/admin/admins/update")
    public String updateAdmin(@RequestParam("oldEmail") String oldEmail,
                              @RequestParam("name") String name,
                              @RequestParam("email") String email,
                              @RequestParam("password") String password,
                              @RequestParam("role") String role,
                              Model model) {
        Admin updatedAdmin = new Admin(null, name, email, password, role);
        boolean success = adminManagement.updateAdmin(oldEmail, updatedAdmin);
        if (success) {
            return "redirect:/admin?tab=admins&updatedAdmin=true";
        } else {
            return "redirect:/admin?tab=admins&errorUpdateAdmin=true";
        }
    }

    @PostMapping("/admin/admins/delete")
    public String deleteAdmin(@RequestParam("email") String email, Model model) {
        boolean deleted = adminManagement.deleteAdmin(email);
        if (deleted) {
            return "redirect:/admin?tab=admins&deletedAdmin=true";
        } else {
            return "redirect:/admin?tab=admins&errorDeleteAdmin=true";
        }
    }

    
    
    

    
    @GetMapping({ "/bikes", "/bike.html" })
    public String showBikesDashboard() {
        return "bikes"; 
    }
}
