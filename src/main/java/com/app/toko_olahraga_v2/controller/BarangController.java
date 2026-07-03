package com.app.toko_olahraga_v2.controller;

import com.app.toko_olahraga_v2.model.Barang;
import com.app.toko_olahraga_v2.model.Akun;
import com.app.toko_olahraga_v2.model.LogAktivitas;
import com.app.toko_olahraga_v2.service.BarangService;
import com.app.toko_olahraga_v2.service.LogAktivitasService;
import com.app.toko_olahraga_v2.service.AuthService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.dao.DataIntegrityViolationException;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/barang")
public class BarangController {

    private final BarangService barangService;
    private final LogAktivitasService logAktivitasService;
    private final AuthService authService;

    public BarangController(BarangService barangService, LogAktivitasService logAktivitasService, AuthService authService) {
        this.barangService = barangService;
        this.logAktivitasService = logAktivitasService; 
        this.authService = authService;
    }


    @GetMapping
    public String index(
            @org.springframework.web.bind.annotation.RequestParam(required = false) String keyword,
            @org.springframework.web.bind.annotation.RequestParam(required = false, defaultValue = "desc") String sort,
            Model model) {
        
        model.addAttribute("listBarang", barangService.getAll(keyword, sort));
        model.addAttribute("barang", new Barang()); 
        model.addAttribute("mode", null);
        
        // Return back to view to keep the form state
        model.addAttribute("keyword", keyword);
        model.addAttribute("sort", sort);

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
    public String simpan(@ModelAttribute Barang barang, RedirectAttributes redirectAttributes, HttpSession session) {
        barangService.tambahBarang(barang);

        try {
            Integer idAkun = (Integer) session.getAttribute("idAkun");
            Akun akun = (idAkun != null) ? authService.getAkunById(idAkun) : null;
            LogAktivitas log = new LogAktivitas();
            log.setAkun(akun);
            log.setAksi("Menambahkan barang baru: " + barang.getNamaBarang() + " (Stok: " + barang.getStok() + ", Harga: Rp " + String.format("%,.0f", barang.getHargaJual()) + ")");
            logAktivitasService.addLog(log);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
    public String update(@ModelAttribute Barang barang, RedirectAttributes redirectAttributes, HttpSession session) {
        barangService.tambahBarang(barang);

        try {
            Integer idAkun = (Integer) session.getAttribute("idAkun");
            Akun akun = (idAkun != null) ? authService.getAkunById(idAkun) : null;
            LogAktivitas log = new LogAktivitas();
            log.setAkun(akun);
            log.setAksi("Mengedit barang: " + barang.getNamaBarang() + " (Stok: " + barang.getStok() + ", Harga: Rp " + String.format("%,.0f", barang.getHargaJual()) + ")");
            logAktivitasService.addLog(log);
        } catch (Exception e) {
            e.printStackTrace();
        }

        redirectAttributes.addFlashAttribute("pesan", "Data barang berhasil diperbarui!");
        redirectAttributes.addFlashAttribute("tipePesan", "edit");
        return "redirect:/barang";
    }

    @GetMapping("/hapus/{idBarang}")
    public String hapus(@PathVariable int idBarang, RedirectAttributes redirectAttributes, HttpSession session) {
        try {
            Barang barang = barangService.getBarangById(idBarang);
            String namaBarang = (barang != null) ? barang.getNamaBarang() : "Tidak Diketahui";

            barangService.hapusBarang(idBarang);

            try {
                Integer idAkun = (Integer) session.getAttribute("idAkun");
                Akun akun = (idAkun != null) ? authService.getAkunById(idAkun) : null;
                LogAktivitas log = new LogAktivitas();
                log.setAkun(akun);
                log.setAksi("Menghapus barang: " + namaBarang + " (ID: " + idBarang + ")");
                logAktivitasService.addLog(log);
            } catch (Exception logEx) {
                logEx.printStackTrace();
            }

            redirectAttributes.addFlashAttribute("pesan", "Data barang berhasil dihapus!");
            redirectAttributes.addFlashAttribute("tipePesan", "delete");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("pesan", "Gagal menghapus: Barang sudah ada di transaksi!");
            redirectAttributes.addFlashAttribute("tipePesan", "delete");
        }
        return "redirect:/barang";
    }
}