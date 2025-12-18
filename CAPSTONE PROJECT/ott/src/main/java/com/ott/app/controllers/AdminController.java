package com.ott.app.controllers;

import com.ott.app.repositories.UserRepository;
import com.ott.app.entities.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // =========================
    // ADMIN LOGIN PAGE
    // =========================
    @GetMapping("/login")
    public String adminLoginPage() {
        return "admin-login";
    }

    // =========================
    // ADMIN LOGIN SUBMIT
    // =========================
    @PostMapping("/login")
    public String adminLogin(
            @RequestParam String email,
            @RequestParam String password,
            Model model
    ) {

        User user = userRepository.findByEmail(email);

        if (user == null ||
            !"ADMIN".equals(user.getRole()) ||
            !passwordEncoder.matches(password, user.getPassword())) {

            model.addAttribute("error", "Invalid admin credentials");
            return "admin-login";
        }

        return "redirect:/admin/dashboard";
    }

    // =========================
    // ADMIN DASHBOARD
    // =========================
    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "admin";
    }
}