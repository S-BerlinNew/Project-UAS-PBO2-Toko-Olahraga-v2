package com.app.toko_olahraga_v2.model;

import jakarta.persistence.*;

@Entity
@Table(name = "barang")

public class Barang {
    //atribut
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idBarang;
    private String namaBarang;
    private double hargaJual;
    private double hargaModal;
    private String jenisBarang;
    private String brand;
    private String warna;
    private int stok;
    private int status;
    private String kodeBarang;

    //konstruktor
    public Barang (int idBarang, String namaBarang, double hargaJual, double hargaModal, String jenisBarang,
                    String brand, String warna, int stok, int status, String kodeBarang) {

        this.idBarang = idBarang;
        this.namaBarang = namaBarang;
        this.hargaJual = hargaJual;
        this.hargaModal = hargaModal;
        this.jenisBarang = jenisBarang;
        this.brand = brand;
        this.warna = warna;
        this.stok = stok;
        this.status = status;
        this.kodeBarang = kodeBarang;
    }

    //Fetching
    public Barang() {

    }

    //Method
    public int getIdBarang() {
        return idBarang;
    }
    public void setIdBarang(int idBarang) {
        this.idBarang = idBarang;
    }

    public String getNamaBarang() {
        return namaBarang;
    }
    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public double getHargaJual() {
        return hargaJual;
    }
    public void setHargaJual(double hargaJual) {
        this.hargaJual = hargaJual;
    }

    public double getHargaModal() {
        return hargaModal;
    }
    public void setHargaModal(double hargaModal) {
        this.hargaModal = hargaModal;
    }

    public String getJenisBarang() {
        return jenisBarang;
    }
    public void setJenisBarang(String jenisBarang) {
        this.jenisBarang = jenisBarang;
    }

    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getWarna() {
        return warna;
    }
    public void setWarna(String warna) {
        this.warna = warna;
    }

    public int getStok() {
        return stok;
    }
    public void setStok(int stok) {
        this.stok = stok;
    }

    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }

    public String getKodeBarang() {
        return kodeBarang;
    }
    public void setKodeBarang(String kodeBarang) {
        this.kodeBarang = kodeBarang;
    }
}
