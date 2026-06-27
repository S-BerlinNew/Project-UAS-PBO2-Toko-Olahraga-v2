package com.app.toko_olahraga_v2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.app.toko_olahraga_v2.repository.BarangRepository;
import com.app.toko_olahraga_v2.repository.CustomerRepository;
import com.app.toko_olahraga_v2.repository.PenjualanRepository;
import com.app.toko_olahraga_v2.repository.AkunRepository;

import java.text.NumberFormat;
import java.util.Locale;

import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {

    @Autowired
    private BarangRepository barangRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PenjualanRepository penjualanRepository;

    @Autowired
    private AkunRepository akunRepository;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Object user = session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/";
        }

        model.addAttribute("username", user);
        
        // Fetch Summary Data
        long totalCustomer = customerRepository.count();
        Double pendapatanDbl = penjualanRepository.sumTotalPendapatanBulanIni();
        double pendapatan = pendapatanDbl != null ? pendapatanDbl : 0.0;
        long totalTransaksi = penjualanRepository.count();
        long stokMenipis = barangRepository.countByStokLessThan(5);

        // Format Rupiah
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        String pendapatanFormatted = formatRupiah.format(pendapatan).replace(",00", "");

        model.addAttribute("totalCustomer", totalCustomer);
        model.addAttribute("pendapatan", pendapatanFormatted);
        model.addAttribute("totalTransaksi", totalTransaksi);
        model.addAttribute("stokMenipis", stokMenipis);

        // Fetch Data for Tables (top 5)
        model.addAttribute("listBarang", barangRepository.findAll().stream().limit(5).toList());
        model.addAttribute("listCustomer", customerRepository.findAll().stream().limit(5).toList());
        model.addAttribute("listAkun", akunRepository.findAll().stream().limit(5).toList());

        return "dashboard";
    }
}

