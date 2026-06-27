package com.app.toko_olahraga_v2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.dao.DataIntegrityViolationException;

import com.app.toko_olahraga_v2.model.Barang;
import com.app.toko_olahraga_v2.service.BarangService;


@Controller
@RequestMapping("/barang")
public class BarangController {

    private final BarangService barangService;

    public BarangController(BarangService barangService) {
        this.barangService = barangService;
    }


    @GetMapping
    public String index(Model model) {
        // model.addAttribute("listBarang", barangService.getAll());
        model.addAttribute("listBarang", barangService.getAll());
        model.addAttribute("barang", new Barang()); 
        model.addAttribute("mode", null);

        return "barang/index";
    }

    @GetMapping("/tambah")
    public String tambahForm(Model model) {

        model.addAttribute("listBarang", barangService.getAll());
        model.addAttribute("barang", new Barang());
        model.addAttribute("mode", "tambah");

        return "barang/index";
    }

    @PostMapping("/simpan")
    public String simpan(@ModelAttribute Barang barang, RedirectAttributes redirectAttributes) {
        barangService.tambahBarang(barang);
        redirectAttributes.addFlashAttribute("pesan", "Data barang berhasil ditambahkan!");
        redirectAttributes.addFlashAttribute("tipePesan", "success");
        return "redirect:/barang";
    }

    @GetMapping("/edit/{idBarang}")
    public String editForm(
            @PathVariable int idBarang,
            Model model) {

        model.addAttribute("listBarang", barangService.getAll());
        model.addAttribute("barang", barangService.getBarangById(idBarang));
        model.addAttribute("mode", "edit");
        return "barang/index";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Barang barang, RedirectAttributes redirectAttributes) {
        barangService.tambahBarang(barang);
        redirectAttributes.addFlashAttribute("pesan", "Data barang berhasil diperbarui!");
        redirectAttributes.addFlashAttribute("tipePesan", "edit");
        return "redirect:/barang";
    }

    @GetMapping("/hapus/{idBarang}")
    public String hapus(@PathVariable int idBarang, RedirectAttributes redirectAttributes) {
        try {
            barangService.hapusBarang(idBarang);
            redirectAttributes.addFlashAttribute("pesan", "Data barang berhasil dihapus!");
            redirectAttributes.addFlashAttribute("tipePesan", "delete");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("pesan", "Gagal menghapus: Barang sudah ada di transaksi!");
            redirectAttributes.addFlashAttribute("tipePesan", "delete");
        }
        return "redirect:/barang";
    }
}