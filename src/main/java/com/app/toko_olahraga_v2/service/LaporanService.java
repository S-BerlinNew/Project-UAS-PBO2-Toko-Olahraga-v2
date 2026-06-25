package com.app.toko_olahraga_v2.service;

import org.springframework.stereotype.Service;
import java.util.List;

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
}
