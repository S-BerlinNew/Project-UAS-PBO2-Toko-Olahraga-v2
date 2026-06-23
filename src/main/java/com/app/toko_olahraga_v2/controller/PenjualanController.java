package com.app.toko_olahraga_v2.controller;

import com.app.toko_olahraga_v2.model.Penjualan;
import com.app.toko_olahraga_v2.model.Customer;
import com.app.toko_olahraga_v2.model.DetailPenjualan;
import com.app.toko_olahraga_v2.service.BarangService;
import com.app.toko_olahraga_v2.service.CustomerService;
import com.app.toko_olahraga_v2.service.PenjualanService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PenjualanController {

    private final PenjualanService penjualanService;
    private final CustomerService customerService;
    private final BarangService barangService;

    public PenjualanController(PenjualanService penjualanService, CustomerService customerService, BarangService barangService) {
        this.penjualanService = penjualanService;
        this.customerService = customerService;
        this.barangService = barangService;
    }

    // 1. MEMBUKA HALAMAN UTAMA KASIR (Saat pertama kali diklik dari Dashboard)
    @GetMapping("/penjualan")
    public String index(Model model) {
        // Ambil data semua customer dari database untuk keperluan modal cari member
        List<Customer> customers = customerService.getAll(); 
        
        // Oper list customer ke Thymeleaf dengan nama "listCustomer"
        model.addAttribute("listCustomer", customers);

        // Oper list barang thymeleaf dengan nama "listBarang"
        model.addAttribute("listBarang", barangService.getAllAktif());
        
        // Sediakan objek Penjualan kosong baru agar form th:object="${penjualan}" tidak meledak
        model.addAttribute("penjualan", new Penjualan());
        
        // Set nilai default total bayar di box tampilan depan
        model.addAttribute("totalBayar", 0);

        // Arahkan ke file templates/penjualan/index.html
        return "penjualan/index";
    }

    // 2. MENERIMA SUBMIT FORM TRANSAKSI DARI KASIR
    @PostMapping("/penjualan/bayar")
    public String prosesBayar(@ModelAttribute("penjualan") Penjualan penjualan, Model model) {
        try {
            // Tempat penampungan keranjang belanja sementara
            // Catatan: Nanti listKeranjang ini datanya akan diparsing dari input hidden jsonKeranjang
            List<DetailPenjualan> listKeranjang = new ArrayList<>(); 

            // Eksekusi logika transaksi di service yang kita perbaiki tadi
            Penjualan penjualanSaved = penjualanService.prosesTransaksi(penjualan, listKeranjang);

            // Jika berhasil, kirim data hasil transaksi yang sudah ada nomor nota dan kembaliannya ke halaman nota/sukses
            model.addAttribute("penjualan", penjualanSaved);

            // Arahkan ke halaman fragment pembayaran atau halaman sukses transaksi
            return "fragments/pembayaran"; 

        } catch (Exception e) {
            // Jika transaksi gagal (misal uang kurang atau stok habis), kembalikan ke halaman kasir tanpa bikin error 500
            // Suapi ulang data yang dibutuhkan oleh Thymeleaf index.html biar gak null pointer
            model.addAttribute("listCustomer", customerService.getAll());
            model.addAttribute("penjualan", penjualan); // Kembalikan data form yang sempat diinput kasir
            
            // Kirim pesan errornya agar bisa muncul di layar berupa alert/text
            model.addAttribute("error", e.getMessage());
            
            return "penjualan/index";
        }
    }
    
    // 3. TOMBOL "TRANSAKSI SELESAI" ATAU "RESET" DI KLIK
    @PostMapping("/penjualan/selesai")
    public String transaksiSelesai() {
        // Bersihkan halaman dan redirect balik ke halaman kasir polosan
        return "redirect:/penjualan"; 
    }
}