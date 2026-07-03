package com.app.toko_olahraga_v2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.toko_olahraga_v2.model.LogAktivitas;

import java.util.List;

@Repository
public interface LogAktivitasRepository extends JpaRepository<LogAktivitas, Integer> {
    // ========MENGAMBIL SEMUA DATA DI TABEL LOG AKTIVITAS========
    @Query("SELECT l FROM LogAktivitas l ORDER BY l.waktu DESC")
    List<LogAktivitas> findAll();   
}
