package com.app.toko_olahraga_v2.model;

import jakarta.persistence.*;

@Entity
@Table(name = "akun")

public class Akun {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAkun;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;
    
    private String namaLengkap;

    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role {
        ADMIN, KASIR
    }

    // Constructor kosong -JPA
    public Akun() {
    }

    // getter & setter
    public int getIdAkun() {
        return idAkun;
    }
    public void setIdAkun(int idAkun) {
        this.idAkun = idAkun;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }
    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }
}
