package com.app.toko_olahraga_v2.service;

import  com.app.toko_olahraga_v2.model.LogAktivitas;
import com.app.toko_olahraga_v2.repository.LogAktivitasRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogAktivitasService {
    
    private final LogAktivitasRepository logAktivitasRepository;

    public LogAktivitasService(LogAktivitasRepository logAktivitasRepository) {
        this.logAktivitasRepository = logAktivitasRepository;
    }

    // ==========AMBIL SEMUA AKTIVITAS========
    public List<LogAktivitas> getAll() {
        return logAktivitasRepository.findAll();
    }

    //==========SIMPAN AKTIVITAS==========
    public void addLog(LogAktivitas log) {
        logAktivitasRepository.save(log);
    }   
}
