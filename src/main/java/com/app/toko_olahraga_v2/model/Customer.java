package com.app.toko_olahraga_v2.model;

import jakarta.persistence.*;

@Entity
@Table(name = "customer")

public class Customer {
    //atribut
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCustomer;
    private String kodeCustomer;
    private String namaCustomer;
    private String noTelepon;

    //kontruktor
    public Customer (Integer idCustomer, String kodeCustomer, String namaCustomer, String noTelepon) {
        this.idCustomer = idCustomer;
        this.kodeCustomer = kodeCustomer;
        this.namaCustomer = namaCustomer;
        this.noTelepon = noTelepon;
    }

    // fetching
    public Customer() {

    }

    // Getter & Setter
    public Integer getIdCustomer() {
        return idCustomer;
    }
    public void setIdCustomer(Integer idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getKodeCustomer() {
        return kodeCustomer;
    }
    public void setKodeCustomer(String kodeCustomer) {
        this.kodeCustomer = kodeCustomer;
    }

    public String getNamaCustomer() {
        return namaCustomer;
    }
    public void setNamaCustomer(String namaCustomer) {
        this.namaCustomer = namaCustomer;
    }

    public String getNoTelepon() {
        return noTelepon;
    }
    public void setNoTelepon(String noTelepon) {
        this.noTelepon= noTelepon;
    }
}
