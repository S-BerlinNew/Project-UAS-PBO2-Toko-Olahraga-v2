package com.app.toko_olahraga_v2.repository;

import com.app.toko_olahraga_v2.model.Akun;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AkunRepository extends JpaRepository<Akun, Integer> {
    Optional<Akun> findByUsernameAndPassword(String username, String password);

    // ========PENCARIAN DAN SORTING=========
    @org.springframework.data.jpa.repository.Query("SELECT a FROM Akun a WHERE LOWER(a.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(a.namaLengkap) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    java.util.List<Akun> searchAkun(@org.springframework.data.repository.query.Param("keyword") String keyword, org.springframework.data.domain.Sort sort);
}