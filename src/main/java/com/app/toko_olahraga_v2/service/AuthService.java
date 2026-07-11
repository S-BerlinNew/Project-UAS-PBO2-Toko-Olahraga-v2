package com.app.toko_olahraga_v2.service;

import com.app.toko_olahraga_v2.model.Akun;
import com.app.toko_olahraga_v2.repository.AkunRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    private final AkunRepository akunRepository;

    public AuthService(AkunRepository akunRepository) {
        this.akunRepository = akunRepository;
    }

    // ======AMBIL SEMUA DATA AKUN======
    public List<Akun> getAll() {
        return akunRepository.findAll(org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "idAkun"));
    }

    // ======AMBIL SEMUA DATA AKUN DENGAN PENCARIAN & SORTING======
    public List<Akun> getAll(String keyword, String sortDirection) {
        org.springframework.data.domain.Sort.Direction direction = 
            "asc".equalsIgnoreCase(sortDirection) ? org.springframework.data.domain.Sort.Direction.ASC : org.springframework.data.domain.Sort.Direction.DESC;
        
        org.springframework.data.domain.Sort sort = org.springframework.data.domain.Sort.by(direction, "idAkun");
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            return akunRepository.searchAkun(keyword, sort);
        }
        return akunRepository.findAll(sort);
    }

    // =======AMBIL AKUN ID=======
    public Akun getAkunById(int idAkun) {
        return akunRepository.findById(idAkun).orElse(null);
    }

    // =======SIMPAN/EDIT DATA AKUN (dengan hash password)========
    public void tambahAkun(Akun akun) {
        akunRepository.save(akun);
    }

    // ======HAPUS AKUN========
    public void hapusAkun(int idAkun) {
        akunRepository.deleteById(idAkun);
    }

    // =======PENGECEKAN ROLE LOGIN========
    public Akun prosesLogin(String username, String password) {
        Optional<Akun> akunOptional = akunRepository.findByUsernameAndPassword(username, password);

        if (akunOptional.isPresent()) {
            return akunOptional.get(); 
        }
        return null;
    }
}