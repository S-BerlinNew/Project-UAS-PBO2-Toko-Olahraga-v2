package com.app.toko_olahraga_v2.controller;

import com.app.toko_olahraga_v2.model.Penjualan;
import com.app.toko_olahraga_v2.model.Customer;
import com.app.toko_olahraga_v2.model.DetailPenjualan;
import com.app.toko_olahraga_v2.model.Akun;
import com.app.toko_olahraga_v2.model.Barang;
import com.app.toko_olahraga_v2.service.BarangService;
import com.app.toko_olahraga_v2.service.CustomerService;
import com.app.toko_olahraga_v2.service.PenjualanService;
import com.app.toko_olahraga_v2.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PenjualanController {

    private final PenjualanService penjualanService;
    private final CustomerService customerService;
    private final BarangService barangService;
    private final AuthService authService;

    public PenjualanController(PenjualanService penjualanService, CustomerService customerService,
            BarangService barangService, AuthService authService) {
        this.penjualanService = penjualanService;
        this.customerService = customerService;
        this.barangService = barangService;
        this.authService = authService;
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

        // Sediakan objek Penjualan kosong baru agar form th:object="${penjualan}" tidak
        // meledak
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
            // Catatan: Nanti listKeranjang ini datanya akan diparsing dari input hidden
            // jsonKeranjang
            List<DetailPenjualan> listKeranjang = new ArrayList<>();

            // Eksekusi logika transaksi di service yang kita perbaiki tadi
            Penjualan penjualanSaved = penjualanService.prosesTransaksi(penjualan, listKeranjang);

            // Jika berhasil, kirim data hasil transaksi yang sudah ada nomor nota dan
            // kembaliannya ke halaman nota/sukses
            model.addAttribute("penjualan", penjualanSaved);

            // Arahkan ke halaman fragment pembayaran atau halaman sukses transaksi
            return "fragments/pembayaran";

        } catch (Exception e) {
            // Jika transaksi gagal (misal uang kurang atau stok habis), kembalikan ke
            // halaman kasir tanpa bikin error 500
            // Suapi ulang data yang dibutuhkan oleh Thymeleaf index.html biar gak null
            // pointer
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

    // 4. MEMUAT MODAL PEMBAYARAN SECARA DINAMIS VIA AJAX GET
    @GetMapping("/penjualan/pembayaran")
    public String showPembayaran(
            @RequestParam("metode") String metode,
            @RequestParam(value = "customerId", required = false) Integer customerId,
            @RequestParam("totalBayar") double totalBayar,
            @RequestParam("tunaiDiterima") double tunaiDiterima,
            @RequestParam("idAkun") Integer idAkun,
            Model model) {

        model.addAttribute("metode", metode);
        model.addAttribute("customerId", customerId);
        model.addAttribute("totalBayar", totalBayar);
        model.addAttribute("tunaiDiterima", tunaiDiterima);
        model.addAttribute("idAkun", idAkun);

        return "fragments/pembayaran";
    }

    // 5. MENERIMA SUBMIT DATA TRANSAKSI FINAL VIA AJAX POST JSON
    @PostMapping("/penjualan/simpan-final")
    @ResponseBody
    public ResponseEntity<?> simpanFinal(@RequestBody TransactionDto dto) {
        try {
            Penjualan penjualan = new Penjualan();

            // Set Akun Kasir
            if (dto.idAkun() != null) {
                Akun akun = authService.getAkunById(dto.idAkun());
                penjualan.setAkun(akun);
            }
            if (penjualan.getAkun() == null) {
                List<Akun> allAkun = authService.getAll();
                if (!allAkun.isEmpty()) {
                    penjualan.setAkun(allAkun.get(0));
                }
            }

            // Set Customer
            if (dto.customerId() != null) {
                Customer customer = customerService.getCustomerById(dto.customerId());
                penjualan.setCustomer(customer);
            }

            penjualan.setMetodePembayaran(dto.metodePembayaran());
            penjualan.setTotalBayar(dto.totalBayar());
            penjualan.setJumlahBayar(dto.jumlahBayar());
            penjualan.setUangKembali(dto.uangKembali());

            // Set Detail Penjualan items
            List<DetailPenjualan> listKeranjang = new ArrayList<>();
            if (dto.items() != null) {
                for (TransactionItemDto itemDto : dto.items()) {
                    DetailPenjualan detail = new DetailPenjualan();

                    Barang barang = new Barang();
                    barang.setIdBarang(itemDto.idBarang());
                    detail.setBarang(barang);

                    detail.setQty(itemDto.qty());
                    detail.setJumlah(itemDto.qty()); // Set jumlah = qty untuk menjaga integritas database

                    // Diskon disimpan sebagai Rupiah absolut dalam PenjualanService
                    double diskonRupiah = (itemDto.hargaJual() * itemDto.qty()) * (itemDto.diskonPersen() / 100.0);
                    detail.setDiskon(diskonRupiah);
                    detail.setHargaJual(itemDto.hargaJual());

                    listKeranjang.add(detail);
                }
            }

            penjualanService.prosesTransaksi(penjualan, listKeranjang);
            return ResponseEntity.ok("Transaksi berhasil disimpan.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Helper records for JSON serialization/deserialization
    public record TransactionItemDto(
            int idBarang,
            String namaBarang,
            double hargaJual,
            int qty,
            String brand,
            double diskonPersen,
            double subtotal) {
    }

    public record TransactionDto(
            Integer idAkun,
            Integer customerId,
            String metodePembayaran,
            double totalBayar,
            double jumlahBayar,
            double uangKembali,
            List<TransactionItemDto> items) {
    }
}