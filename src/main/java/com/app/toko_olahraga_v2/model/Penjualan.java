package com.app.toko_olahraga_v2.model;

import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "penjualan")
public class Penjualan {

    // Atribut
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_penjualan")
    private int idPenjualan;

    // HUBUNGAN KE TABEL AKUN (KASIR)
    @ManyToOne
    @JoinColumn(name = "id_akun") 
    private Akun akun;

    // HUBUNGAN KE TABEL CUSTOMER
    @ManyToOne
    @JoinColumn(name = "id_customer") 
    private Customer customer;

    @Column(name = "no_nota", unique = true, nullable = false, length = 50)
    private String noNota;

    @Column(name = "tanggal", nullable = false)
    private LocalDateTime tanggal;

    @Column(name = "metode_pembayaran", length = 30)
    private String metodePembayaran;

    @Column(name = "total_bayar")
    private double totalBayar; 

    @Column(name = "jumlah_bayar")
    private double jumlahBayar; 

    @Column(name = "uang_tunai")
    private double uangTunai; 

    @Column(name = "uang_kembali")
    private double uangKembali; 

    @OneToMany(mappedBy = "penjualan", fetch = FetchType.EAGER)
    private List<DetailPenjualan> detailPenjualan;

    // fetching
    public Penjualan() {
    }

    // Konstruktor
    public Penjualan(Akun akun, Customer customer, String noNota, LocalDateTime tanggal, 
                     String metodePembayaran, double totalBayar, double jumlahBayar, double uangKembali) {
        this.akun = akun;
        this.customer = customer;
        this.noNota = noNota;
        this.tanggal = tanggal;
        this.metodePembayaran = metodePembayaran;
        this.totalBayar = totalBayar;
        this.jumlahBayar = jumlahBayar;
        this.uangKembali = uangKembali;
    }

    // Getter & Stter
    public int getIdPenjualan() {
        return idPenjualan;
    }
    public void setIdPenjualan(int idPenjualan) {
        this.idPenjualan = idPenjualan;
    }

    public Akun getAkun() {
        return akun;
    }
    public void setAkun(Akun akun) {
        this.akun = akun;
    }

    public Customer getCustomer() {
        return customer;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getNoNota() {
        return noNota;
    }
    public void setNoNota(String noNota) {
        this.noNota = noNota;
    }

    public LocalDateTime getTanggal() {
        return tanggal;
    }
    public void setTanggal(LocalDateTime tanggal) {
        this.tanggal = tanggal;
    }

    public String getMetodePembayaran() {
        return metodePembayaran;
    }
    public void setMetodePembayaran(String metodePembayaran) {
        this.metodePembayaran = metodePembayaran;
    }

    public double getTotalBayar() {
        return totalBayar;
    }
    public void setTotalBayar(double totalBayar) {
        this.totalBayar = totalBayar;
    }

    public double getJumlahBayar() {
        return jumlahBayar;
    }
    public void setJumlahBayar(double jumlahBayar) {
        this.jumlahBayar = jumlahBayar;
    }

    public double getUangKembali() {
        return uangKembali;
    }
    public void setUangKembali(double uangKembali) {
        this.uangKembali = uangKembali;
    }

    public double getUangTunai() {
        return uangTunai;
    }
    public void setUangTunai(double uangTunai) {
        this.uangTunai = uangTunai;
    }

    public List<DetailPenjualan> getDetailPenjualan() {
        return detailPenjualan;
    }
    public void setDetailPenjualan(List<DetailPenjualan> detailPenjualan) {
        this.detailPenjualan = detailPenjualan;
    }

    public double getKeuntungan() {
        if (detailPenjualan == null) return 0;
        double totalKeuntungan = 0;
        for (DetailPenjualan detail : detailPenjualan) {
            if (detail.getBarang() != null) {
                double modal = detail.getBarang().getHargaModal() * detail.getQty();
                totalKeuntungan += (detail.getSubtotal() - modal);
            }
        }
        return totalKeuntungan;
    }
}