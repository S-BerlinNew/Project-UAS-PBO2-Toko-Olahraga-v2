package com.app.toko_olahraga_v2.service;

import com.app.toko_olahraga_v2.model.Customer;
import com.app.toko_olahraga_v2.repository.CustomerRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // =======AMBIL SEMUA DATA CUSTOMER========
    public List<Customer> getAll() {
        return customerRepository.findAll(org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "idCustomer"));
    }

    // =======AMBIL DATA CUSTOMER DENGAN PENCARIAN & SORTING========
    public List<Customer> getAll(String keyword, String sortDirection) {
        org.springframework.data.domain.Sort.Direction direction = 
            "asc".equalsIgnoreCase(sortDirection) ? org.springframework.data.domain.Sort.Direction.ASC : org.springframework.data.domain.Sort.Direction.DESC;
        
        org.springframework.data.domain.Sort sort = org.springframework.data.domain.Sort.by(direction, "idCustomer");
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            return customerRepository.searchCustomer(keyword, sort);
        }
        return customerRepository.findAll(sort);
    }

    // =======AMBIL CUSTOMER NAMA=========
    public List<Customer> getCustomerByNama(String namaCustomer) {
        return customerRepository.findByNamaCustomerContainingIgnoreCase(namaCustomer);
    }

    //========AMBIL CUSTOMER KODE=========
    public Customer getCustomerByKode(String kodeCustomer) {
        return customerRepository.findByKodeCustomer(kodeCustomer);
    }

    //========AMBIL CUSTOMER ID==========
    public Customer getCustomerById(int idCustomer) {
        return customerRepository.findById(idCustomer).orElse(null);
    }

    // =======TAMBAH DATA CUSTOMER=======
    public void tambahCustomer(Customer customer) {
        // SAKLAR PENGAMAL: Kalau kodeCustomer kosong/null dari AJAX, kita isi otomatis di sini
        if (customer.getKodeCustomer() == null || customer.getKodeCustomer().trim().isEmpty()) {
            customer.setKodeCustomer(generateKodeCustomerOtomatis());
        }
        
        customerRepository.save(customer);
    }

    // =======HAPUS DATA CUSTOMER========
    public void hapusCustomer(int idCustomer) {
        customerRepository.deleteById(idCustomer);
    }

    // ======= FUNGSI GENERATE KODE CUSTOMER OTOMATIS =======
    // Fungsi ini bakal bikin kode format: CS-20260615-001 (contohnya)
    public String generateKodeCustomerOtomatis() {
        // Ambil total data customer sekarang untuk base nomor urut
        long totalCustomer = customerRepository.count();
        
        // Naikin 1 angka buat customer baru
        long nomorUrut = totalCustomer + 1;
        
        // Bikin format string: CS- diikutin nomor urut (contoh: CS-005)
        // %03d artinya nomornya bakal ada padding 3 digit (001, 002, dst)
        return String.format("CS-%03d", nomorUrut);
    }
    
}
