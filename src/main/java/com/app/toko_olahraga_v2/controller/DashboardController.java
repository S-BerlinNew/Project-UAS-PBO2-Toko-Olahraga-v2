package com.app.toko_olahraga_v2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Object user = session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/";
        }

        model.addAttribute("username", user);
        return "dashboard";
    }
}

