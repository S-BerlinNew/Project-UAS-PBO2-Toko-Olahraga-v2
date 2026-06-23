package com.app.toko_olahraga_v2.repository;

import com.app.toko_olahraga_v2.model.DetailPenjualan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DetailPenjualanRepository extends JpaRepository<DetailPenjualan, Integer> {

    // Cari semua daftar barang lepasan yang dibeli berdasarkan ID Penjualan Induknya
    List<DetailPenjualan> findByPenjualanIdPenjualan(int idPenjualan);

    
}