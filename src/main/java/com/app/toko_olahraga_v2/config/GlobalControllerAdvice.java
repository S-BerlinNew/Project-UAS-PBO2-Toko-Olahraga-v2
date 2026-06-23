package com.app.toko_olahraga_v2.config;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute
    public void suntikUsernameKeSemuaHalaman(HttpSession session, Model model) {
        String usernameTeks = (String) session.getAttribute("loggedInUser");
        
        if (usernameTeks != null) {
            // Mengirim data "username" ke Thymeleaf secara otomatis untuk SEMUA rute URL
            model.addAttribute("username", usernameTeks);
        }
    }
}