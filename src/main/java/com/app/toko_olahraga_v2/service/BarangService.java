package com.app.toko_olahraga_v2.service;

import com.app.toko_olahraga_v2.model.Barang;
import com.app.toko_olahraga_v2.repository.BarangRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BarangService {

    private final BarangRepository barangRepository;

    public BarangService(BarangRepository barangRepository) {
        this.barangRepository = barangRepository;
    }
    
    
    // ========AMBIL SEMUA BARANG========
    public List<Barang> getAll() {
        return barangRepository.findAll();
    }

    // ========AMBIL BARANG AKTIF (STATUS=1)========
    public List<Barang> getAllAktif() {
        return barangRepository.findByStatus(1);
    }

    // ========AMBIL SATU BARANG BY ID========
    public Barang getBarangById(int idBarang) {                        
        return barangRepository.findById(idBarang).orElse(null);
    }

    // ========TAMBAH / UPDATE BARANG========
    public void tambahBarang(Barang barang) {
        barangRepository.save(barang);
    }

    // ========HAPUS BARANG========
    public void hapusBarang(int idBarang) {                            
        barangRepository.deleteById(idBarang);
    }

    // ========UPDATE STATUS AKTIF/NONAKTIF========
    public void updateStatus(int idBarang, int status) {
        Barang barang = getBarangById(idBarang);                        
        if (barang != null) {
            barang.setStatus(status);
            barangRepository.save(barang);
        }
    }

    // ========KURANGI STOK========
    public void kurangiStok(int idBarang, int jumlah) {
        barangRepository.kurangiStok(idBarang, jumlah);
    }
}