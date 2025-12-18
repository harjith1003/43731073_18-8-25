package com.ott.app.controllers;

import com.ott.app.entities.User;
import com.ott.app.repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // ---------------- LOGIN PAGE ----------------
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // ---------------- LOGIN SUBMIT ----------------
    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model
    ) {
        User user = userRepository.findByEmail(email);

        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            model.addAttribute("error", "Invalid credentials");
            return "login";
        }

        // 🔑 STORE LOGIN STATE
        session.setAttribute("email", user.getEmail());
        session.setAttribute("role", user.getRole());

        return "redirect:/dashboard";
    }

    // ---------------- REGISTER PAGE ----------------
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    // ---------------- REGISTER SUBMIT ----------------
    @PostMapping("/register")
    public String register(
            @RequestParam String email,
            @RequestParam String password,
            Model model
    ) {
        if (userRepository.findByEmail(email) != null) {
            model.addAttribute("error", "Email already exists");
            return "register";
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("USER");
        user.setSubscribed(false);
        user.setGenres("");

        userRepository.save(user);

        model.addAttribute("success", "Registration successful. Please login.");
        return "login";
    }

    // ---------------- LOGOUT ----------------
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}