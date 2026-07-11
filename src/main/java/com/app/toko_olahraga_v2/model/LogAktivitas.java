package com.app.toko_olahraga_v2.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "log_aktivitas")
public class LogAktivitas {

    // atribut
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_log")
    private int idLog;

    @ManyToOne(fetch = FetchType.EAGER)
    private Akun akun;

    private LocalDateTime waktu;
    private String aksi;

    @PrePersist
    protected void onCreate() {
        this.waktu = LocalDateTime.now();
    }

    //konstructor
    public LogAktivitas(int idLog, Akun akun, LocalDateTime waktu, String aksi) {
        this.idLog = idLog;
        this.akun = akun;
        this.waktu = waktu;
        this.aksi = aksi;
    }   

    //Fetching
    public LogAktivitas() {
        
    }

    //Method
    public int getIdLog() {
        return idLog;
    }
    public void setIdLog(int idLog) {
        this.idLog = idLog;
    }

    public Akun getAkun() {
        return akun;
    }
    public void setAkun(Akun akun) {
        this.akun = akun;
    }

    public LocalDateTime getWaktu() {
        return waktu;
    }
    public void setWaktu(LocalDateTime waktu) {
        this.waktu = waktu;
    }

    public String getAksi() {
        return aksi;
    }
    public void setAksi(String aksi) {
        this.aksi = aksi;
    }
}
