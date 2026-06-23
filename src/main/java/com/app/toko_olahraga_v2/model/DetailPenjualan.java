package com.app.toko_olahraga_v2.model;

import jakarta.persistence.*;

@Entity
@Table(name = "detail_penjualan")
public class DetailPenjualan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detail")
    private int idDetail;

    @ManyToOne
    @JoinColumn(name = "id_penjualan")
    private Penjualan penjualan;

    @ManyToOne
    @JoinColumn(name = "id_barang", nullable = false)
    private Barang barang;

    @Column(name = "qty", nullable = false)
    private int qty;

    @Column(name = "diskon")
    private double diskon;

    @Column(name = "subtotal", nullable = false)
    private double subtotal;

    @Column(name = "harga_jual", nullable = false)
    private double hargaJual;

    @Column(name = "jumlah", nullable = false)
    private int jumlah; // Menampung kolom 'jumlah' sesuai isi database lo

    // Constructor Kosong
    public DetailPenjualan() {
    }

    // Constructor Lengkap
    public DetailPenjualan(Penjualan penjualan, Barang barang, int qty, double diskon, double subtotal, double hargaJual, int jumlah) {
        this.penjualan = penjualan;
        this.barang = barang;
        this.qty = qty;
        this.diskon = diskon;
        this.subtotal = subtotal;
        this.hargaJual = hargaJual;
        this.jumlah = jumlah;
    }

    // Getter & Setter
    public int getIdDetail() {
        return idDetail;
    }
    public void setIdDetail(int idDetail) {
        this.idDetail = idDetail;
    }

    public Penjualan getPenjualan() {
        return penjualan;
    }
    public void setPenjualan(Penjualan penjualan) {
        this.penjualan = penjualan;
    }

    public Barang getBarang() {
        return barang;
    }
    public void setBarang(Barang barang) {
        this.barang = barang;
    }

    public int getQty() {
        return qty;
    }
    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getDiskon() {
        return diskon;
    }
    public void setDiskon(double diskon) {
        this.diskon = diskon;
    }

    public double getSubtotal() {
        return subtotal;
    }
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getHargaJual() {
        return hargaJual;
    }
    public void setHargaJual(double hargaJual) {
        this.hargaJual = hargaJual;
    }

    public int getJumlah() {
        return jumlah;
    }
    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }
}