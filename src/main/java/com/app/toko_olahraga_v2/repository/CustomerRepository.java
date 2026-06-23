package com.app.toko_olahraga_v2.repository;

import com.app.toko_olahraga_v2.model.Customer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    //=========CARI BERDASARKAN NAMA=========
    List<Customer> findByNamaCustomerContainingIgnoreCase(String namaCustomer);

    //=========CARI BERDASARKAN KODE============
    Customer findByKodeCustomer(String kodeCustomer);
}
