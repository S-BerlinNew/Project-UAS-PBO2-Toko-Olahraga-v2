package com.app.toko_olahraga_v2.controller;

import com.app.toko_olahraga_v2.model.Akun;
import com.app.toko_olahraga_v2.model.LogAktivitas;
import com.app.toko_olahraga_v2.repository.AkunRepository;
import com.app.toko_olahraga_v2.service.AuthService;
import com.app.toko_olahraga_v2.service.LogAktivitasService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.dao.DataIntegrityViolationException;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/kelola-akun")
public class AkunController {

    private final AuthService authService;
    private final LogAktivitasService logAktivitasService;

    public AkunController(AuthService authService, AkunRepository akunRepository, LogAktivitasService logAktivitasService) {
        this.authService = authService;
        this.logAktivitasService = logAktivitasService;
    }

    
    @GetMapping()
    public String kelolaAkunPage(
            @org.springframework.web.bind.annotation.RequestParam(required = false) String keyword,
            @org.springframework.web.bind.annotation.RequestParam(required = false, defaultValue = "desc") String sort,
            Model model) {
        
        model.addAttribute("listAkun", authService.getAll(keyword, sort));
        model.addAttribute("akun", new Akun()); 
        
        model.addAttribute("keyword", keyword);
        model.addAttribute("sort", sort);
        
        return "akun/index"; 
    }

    @GetMapping("/tambah")
    public String tambahForm(Model model) {
        model.addAttribute("listAkun", authService.getAll());
        model.addAttribute("akun", new Akun());
        model.addAttribute("mode", "tambah");
        return "akun/index";
    }

    @PostMapping("/simpan")
    public String simpan(@ModelAttribute Akun akun, RedirectAttributes redirectAttributes, HttpSession session) {
        authService.tambahAkun(akun);

        try {
            Integer idAkunSession = (Integer) session.getAttribute("idAkun");
            Akun akunSession = (idAkunSession != null) ? authService.getAkunById(idAkunSession) : null;
            LogAktivitas log = new LogAktivitas();
            log.setAkun(akunSession);
            log.setAksi("Menambahkan akun baru: " + akun.getUsername() + " (Nama: " + akun.getNamaLengkap() + ", Role: " + akun.getRole() + ")");
            logAktivitasService.addLog(log);
        } catch (Exception e) {
            e.printStackTrace();
        }

        redirectAttributes.addFlashAttribute("pesan", "Data akun berhasil ditambahkan!");
        redirectAttributes.addFlashAttribute("tipePesan", "success");
        return "redirect:/kelola-akun";
    }

    @GetMapping("/edit/{idAkun}")
    public String editForm(
        @PathVariable int idAkun, 
        Model model) {

        model.addAttribute("listAkun", authService.getAll());
        model.addAttribute("akun", authService.getAkunById(idAkun));
        model.addAttribute("mode", "edit");

        return "akun/index";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Akun akun, RedirectAttributes redirectAttributes, HttpSession session) {
        authService.tambahAkun(akun);

        try {
            Integer idAkunSession = (Integer) session.getAttribute("idAkun");
            Akun akunSession = (idAkunSession != null) ? authService.getAkunById(idAkunSession) : null;
            LogAktivitas log = new LogAktivitas();
            log.setAkun(akunSession);
            log.setAksi("Mengedit akun: " + akun.getUsername() + " (Nama: " + akun.getNamaLengkap() + ", Role: " + akun.getRole() + ")");
            logAktivitasService.addLog(log);
        } catch (Exception e) {
            e.printStackTrace();
        }

        redirectAttributes.addFlashAttribute("pesan", "Data akun berhasil diperbarui!");
        redirectAttributes.addFlashAttribute("tipePesan", "edit");
        return "redirect:/kelola-akun";
    }
    
    @GetMapping("/hapus/{idAkun}")
    public String hapus(@PathVariable int idAkun, RedirectAttributes redirectAttributes, HttpSession session) {
        try {
            Akun deletedAkun = authService.getAkunById(idAkun);
            String usernameDeleted = (deletedAkun != null) ? deletedAkun.getUsername() : "Tidak Diketahui";

            authService.hapusAkun(idAkun);

            try {
                Integer idAkunSession = (Integer) session.getAttribute("idAkun");
                Akun akunSession = (idAkunSession != null) ? authService.getAkunById(idAkunSession) : null;
                LogAktivitas log = new LogAktivitas();
                log.setAkun(akunSession);
                log.setAksi("Menghapus akun: " + usernameDeleted + " (ID: " + idAkun + ")");
                logAktivitasService.addLog(log);
            } catch (Exception logEx) {
                logEx.printStackTrace();
            }

            redirectAttributes.addFlashAttribute("pesan", "Data akun berhasil dihapus!");
            redirectAttributes.addFlashAttribute("tipePesan", "delete");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("pesan", "Gagal menghapus: Akun sedang digunakan!");
            redirectAttributes.addFlashAttribute("tipePesan", "delete");
        }
        return "redirect:/kelola-akun";
    }
}
