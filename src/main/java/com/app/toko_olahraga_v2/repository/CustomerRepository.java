package com.app.toko_olahraga_v2.repository;

import com.app.toko_olahraga_v2.model.Customer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    //=========CARI BERDASARKAN NAMA=========
    List<Customer> findByNamaCustomerContainingIgnoreCase(String namaCustomer);

    //=========CARI BERDASARKAN KODE============
    Customer findByKodeCustomer(String kodeCustomer);

    // ========PENCARIAN DAN SORTING=========
    @org.springframework.data.jpa.repository.Query("SELECT c FROM Customer c WHERE LOWER(c.namaCustomer) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(c.kodeCustomer) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(c.noTelepon) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Customer> searchCustomer(@org.springframework.data.repository.query.Param("keyword") String keyword, org.springframework.data.domain.Sort sort);
}
