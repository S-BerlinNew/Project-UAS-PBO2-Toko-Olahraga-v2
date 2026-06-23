package com.app.toko_olahraga_v2.repository;

import com.app.toko_olahraga_v2.model.Akun;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AkunRepository extends JpaRepository<Akun, Integer> {
    Optional<Akun> findByUsernameAndPassword(String username, String password);
}