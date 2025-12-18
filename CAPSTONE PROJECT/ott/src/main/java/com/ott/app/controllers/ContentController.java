package com.ott.app.controllers;

import com.ott.app.entities.User;
import com.ott.app.repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContentController {

    @Autowired
    private UserRepository userRepository;

    // ---------------- DASHBOARD ----------------
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {

        String email = (String) session.getAttribute("email");

        if (email == null) {
            return "redirect:/login";
        }

        User user = userRepository.findByEmail(email);

        model.addAttribute("subscribed", user.isSubscribed());
        model.addAttribute("genres", user.getGenres());

        return "dashboard";
    }

    // ---------------- CONTENT PAGES ----------------
    @GetMapping("/movies")
    public String movies() {
        return "movies";
    }

    @GetMapping("/series")
    public String series() {
        return "series";
    }

    @GetMapping("/news")
    public String news() {
        return "news";
    }

    @GetMapping("/sports")
    public String sports() {
        return "sports";
    }
}