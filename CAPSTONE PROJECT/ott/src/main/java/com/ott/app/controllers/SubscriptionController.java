package com.ott.app.controllers;

import com.ott.app.entities.User;
import com.ott.app.repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class SubscriptionController {

    @Autowired
    private UserRepository userRepository;

    // 🔹 SHOW SUBSCRIBE PAGE
    @GetMapping("/subscribe")
    public String subscribePage() {
        return "subscribe";
    }

    // 🔹 PAYMENT (POST ONLY)
    @PostMapping("/payment")
    public String payment(
            @RequestParam(required = false) List<String> genres,
            HttpSession session
    ) {
        String email = (String) session.getAttribute("email");

        if (email == null) {
            return "redirect:/login";
        }

        User user = userRepository.findByEmail(email);

        user.setSubscribed(true);
        user.setGenres(genres != null ? String.join(",", genres) : "");

        userRepository.save(user);

        return "redirect:/success";
    }

    // 🔹 SUCCESS PAGE
    @GetMapping("/success")
    public String success() {
        return "success";
    }
}