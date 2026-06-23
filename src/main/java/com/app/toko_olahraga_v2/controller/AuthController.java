package com.app.toko_olahraga_v2.controller;

import com.app.toko_olahraga_v2.model.Akun;
import com.app.toko_olahraga_v2.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {
    
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // ---- HALAMAN LOGIN UTAMA  ----
    @GetMapping("/")
    public String loginPage() {
        return "login";
    }

    // ---- PROSES LOGIN ----
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {
        
        Akun akunReal = authService.prosesLogin(username, password);

        if (akunReal != null) {

            session.setAttribute("loggedInUser", akunReal.getNamaLengkap());
            session.setAttribute("role", akunReal.getRole().name()); // Mengambil string ADMIN / KASIR
            
            return "redirect:/dashboard";
        } else {

            redirectAttributes.addFlashAttribute("errorPopUp", "Username atau Password salah");
            return "redirect:/";
        }
    }

    // ---- PROSES LOGOUT ----
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();  // Hapus total semua session
        return "redirect:/";
    }
}