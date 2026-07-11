package com.app.toko_olahraga_v2.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String index(
            @RequestParam(value = "tglMulai", required = false) String tglMulai,
            @RequestParam(value = "tglSelesai", required = false) String tglSelesai,
            @RequestParam(value = "metode", required = false) String metode,
            @RequestParam(value = "sort", required = false, defaultValue = "desc") String sort,
            Model model) {

        // Ambil data berdasarkan filter yang diinput user
        List<Penjualan> listPenjualan = laporanService.getLaporanTerFilter(tglMulai, tglSelesai, metode);
        
        // Lakukan sorting data
        if ("asc".equalsIgnoreCase(sort)) {
            listPenjualan.sort((p1, p2) -> p1.getTanggal().compareTo(p2.getTanggal()));
        } else {
            // desc by default
            listPenjualan.sort((p1, p2) -> p2.getTanggal().compareTo(p1.getTanggal()));
        }

        model.addAttribute("listPenjualan", listPenjualan);
        model.addAttribute("totalOmset", laporanService.hitungTotalOmset(listPenjualan));

        // Kirim balik parameter ke HTML agar form input tidak ter-reset otomatis
        // setelah submit
        model.addAttribute("tglMulaiParam", tglMulai);
        model.addAttribute("tglSelesaiParam", tglSelesai);
        model.addAttribute("metodeParam", metode);
        model.addAttribute("sortParam", sort);

        return "laporan/index";
    }
}