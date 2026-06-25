package com.app.toko_olahraga_v2.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.app.toko_olahraga_v2.model.Penjualan;
import com.app.toko_olahraga_v2.repository.PenjualanRepository;

@Service
public class LaporanService {

    private final PenjualanRepository penjualanRepository;

    public LaporanService(PenjualanRepository penjualanRepository) {
        this.penjualanRepository = penjualanRepository;
    }

    // ========== AMBIL SEMUA DATA PENJUALAN ==========
    public List<Penjualan> getAll() {
        return penjualanRepository.findAll();
    }

    // ========= PERHITUNGAN PROFIT ==========
    public double hitungTotalOmset(List<Penjualan> daftarPenjualan) {
        return daftarPenjualan.stream().mapToDouble(Penjualan::getTotalBayar).sum();
    }

    // ========= FILTER TANGGAL DAN METODE BAYAR ==========
    public List<Penjualan> getLaporanTerFilter(String tglMulai, String tglSelesai, String metodePembayaran) {
        List<Penjualan> semuaData = penjualanRepository.findAll();

        return semuaData.stream().filter(p -> {
            boolean matchTanggal = true;
            boolean matchMetode = true;

            // 1. Filter Rentang Tanggal
            if (tglMulai != null && !tglMulai.isEmpty()) {
                LocalDateTime mulai = LocalDateTime.parse(tglMulai + "T00:00:00");
                matchTanggal = matchTanggal && (!p.getTanggal().isBefore(mulai));
            }
            if (tglSelesai != null && !tglSelesai.isEmpty()) {
                LocalDateTime selesai = LocalDateTime.parse(tglSelesai + "T23:59:59");
                matchTanggal = matchTanggal && (!p.getTanggal().isAfter(selesai));
            }

            // 2. Filter Metode Pembayaran
            if (metodePembayaran != null && !metodePembayaran.isEmpty() && !metodePembayaran.equals("SEMUA")) {
                matchMetode = p.getMetodePembayaran() != null
                        && p.getMetodePembayaran().equalsIgnoreCase(metodePembayaran);
            }

            return matchTanggal && matchMetode;
        }).collect(Collectors.toList());
    }
}
