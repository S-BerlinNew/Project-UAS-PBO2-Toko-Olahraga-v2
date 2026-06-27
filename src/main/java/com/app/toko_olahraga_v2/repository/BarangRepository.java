package com.app.toko_olahraga_v2.repository;

import com.app.toko_olahraga_v2.model.Barang;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BarangRepository extends JpaRepository<Barang, Integer>{
    // ========UNTUK SELECT BARANG WHERE STATUS=1=========
    //----hemat memori----
    List<Barang> findByStatus(int status);

    // ========KURANGI STOK==========
    @Modifying//
    @Transactional//ccegah data aman, karena menunggu semua sukses atau dibatalkan
    @Query("UPDATE Barang b SET b.stok = b.stok - :jumlah WHERE b.idBarang = :id")
    void kurangiStok(@Param("id") int id, @Param("jumlah") int jumlah);

    // ========PENCARIAN DAN SORTING=========
    @Query("SELECT b FROM Barang b WHERE LOWER(b.namaBarang) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(b.kodeBarang) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(b.brand) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Barang> searchBarang(@Param("keyword") String keyword, org.springframework.data.domain.Sort sort);

    // ========HITUNG STOK MENIPIS=========
    long countByStokLessThan(int batas);
} 