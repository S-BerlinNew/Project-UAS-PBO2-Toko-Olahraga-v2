package com.app.toko_olahraga_v2.repository;

import com.app.toko_olahraga_v2.model.Penjualan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PenjualanRepository extends JpaRepository<Penjualan, Integer> {
    //=======CARI BERDASARKAN NOTA======
    Optional<Penjualan> findByNoNota(String noNota);

    // ======PENGECEKAN NONOTA SUDAH TERPAKAI/TIDAK==========
    boolean existsByNoNota(String noNota);

    //========PERINGKASAN DATA JAM DETIK - UNTUK GENERATE NOMOR NOTA BERURUTAN PER HARI========
    @Query(value = "SELECT COUNT(*) FROM penjualan WHERE DATE_FORMAT(tanggal, '%Y%m%d') = :tanggal", nativeQuery = true)
    long countByTanggalFormat(@Param("tanggal") String tanggal);
}