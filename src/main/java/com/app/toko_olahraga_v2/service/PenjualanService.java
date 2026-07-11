package com.app.toko_olahraga_v2.service;

import com.app.toko_olahraga_v2.model.Penjualan;
import com.app.toko_olahraga_v2.model.DetailPenjualan;
import com.app.toko_olahraga_v2.model.Barang;
import com.app.toko_olahraga_v2.repository.BarangRepository;
import com.app.toko_olahraga_v2.repository.DetailPenjualanRepository;
import com.app.toko_olahraga_v2.repository.PenjualanRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;



@Service
public class PenjualanService {
    private final PenjualanRepository penjualanRepository;
    private final DetailPenjualanRepository detailPenjualanRepository;
    private final BarangRepository barangRepository;

    public PenjualanService(PenjualanRepository penjualanRepository, DetailPenjualanRepository detailPenjualanRepository, BarangRepository barangRepository) {
        this.penjualanRepository = penjualanRepository;
        this.detailPenjualanRepository = detailPenjualanRepository;
        this.barangRepository = barangRepository;
    }

    // ===================FUNGSI UTAMA KASIR (TRANSAKSI)========================
    @Transactional(rollbackFor = Exception.class)
    public Penjualan prosesTransaksi(Penjualan penjualan, List<DetailPenjualan> listKeranjang) {
        //1. -----------Validasi awal-----------
        if(listKeranjang == null || listKeranjang.isEmpty()) {
            throw new IllegalArgumentException("Transaksi gagal : Keranjang belanja masih kosong!");
        } 
        if(penjualan.getJumlahBayar() <= 0) {
            throw new IllegalArgumentException("Transaksi gagal : Jumlah uang pembayaran tidak valid!");
        }

        //2. -------Set nomor nota dan tanggal------
        penjualan.setNoNota(generateNoNota());
        penjualan.setTanggal(LocalDateTime.now());

        //3.---------Hitung total - proses detail sebelum save----------
        double akumulasiTotalBayar = 0.0;

        for(DetailPenjualan detail : listKeranjang) {
            Barang barangReal = barangRepository.findById(detail.getBarang().getIdBarang())
                                .orElseThrow(() -> new RuntimeException("Barang Tidak Ditemukan!"));
            
            if(barangReal.getStok() < detail.getQty()) {
                throw new RuntimeException("Stok barang [" + barangReal.getNamaBarang() + "] tidak mencukupi!");
            }

            double subtotalAman = (barangReal.getHargaJual() * detail.getQty()) - detail.getDiskon();
            detail.setBarang(barangReal);
            detail.setSubtotal(subtotalAman);
            akumulasiTotalBayar += subtotalAman;
        } 

        //4.------------Validasi uang (CUKUP/TIDAK)---------
        double kembalian = penjualan.getJumlahBayar() - akumulasiTotalBayar;
        if(kembalian < 0) {
            throw new RuntimeException("Transaksi digagalkan! Uang kurang sebesar : Rp" + Math.abs(kembalian));
        }

        //5.------------Set Total Bayar, Kembali, dan Uang Tunai---------------
        penjualan.setTotalBayar(akumulasiTotalBayar);
        penjualan.setUangKembali(kembalian);
        penjualan.setUangTunai(penjualan.getJumlahBayar());

        // 6.------------Simpan Penjualan--------------
        Penjualan penjualanSaved = penjualanRepository.save(penjualan);

        //7.--------------Simpan Detail dan Potong Stok Barang-------------
        for(DetailPenjualan detail : listKeranjang) {
            detail.setPenjualan(penjualanSaved);
            detailPenjualanRepository.save(detail);

            Barang barang = detail.getBarang();
            barang.setStok(barang.getStok() - detail.getQty());
            barangRepository.save(barang);
        }
        return penjualanSaved;

    }


    // ===================GENERATE NO NOTA================
    private String generateNoNota() {
        LocalDateTime sekarang = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formatTanggal = sekarang.format(formatter); // ← fix ThermalFormatter

        long hitungTransaksiHariIni = penjualanRepository.countByTanggalFormat(formatTanggal);
        long nomorUrutNext = hitungTransaksiHariIni + 1;

        return String.format("TRX-%s-%03d", formatTanggal, nomorUrutNext);
    }

    public Penjualan getById(Integer id) {
    return penjualanRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Transaksi tidak ditemukan"));
    }
}
