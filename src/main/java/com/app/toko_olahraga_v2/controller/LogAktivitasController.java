package com.app.toko_olahraga_v2.controller;

import com.app.toko_olahraga_v2.service.LogAktivitasService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogAktivitasController {

    private final LogAktivitasService logAktivitasService;

    public LogAktivitasController(LogAktivitasService logAktivitasService) {
        this.logAktivitasService = logAktivitasService;
    }

    @GetMapping("/log-aktivitas")
    public String index(Model model) {
        model.addAttribute("listLog", logAktivitasService.getAll());
        return "logaktivitas/index";
    }
}