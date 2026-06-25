package com.app.toko_olahraga_v2.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.app.toko_olahraga_v2.model.Penjualan;
import com.app.toko_olahraga_v2.service.LaporanService;

@Controller
public class LaporanController {

    private final LaporanService laporanService;

    // Bersihkan PenjualanService yang tidak terpakai agar constructor aman
    public LaporanController(LaporanService laporanService) {
        this.laporanService = laporanService;
    }

    @GetMapping("/laporan")
    public String index(Model model) {
        List<Penjualan> listPenjualan = laporanService.getAll();

        model.addAttribute("listPenjualan", listPenjualan);
        model.addAttribute("totalOmset", laporanService.hitungTotalOmset(listPenjualan));
        model.addAttribute("penjualan", new Penjualan());
        model.addAttribute("mode", null);

        return "laporan/index";
    }
}