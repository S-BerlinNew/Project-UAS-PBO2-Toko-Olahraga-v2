package com.app.toko_olahraga_v2.controller;

import com.app.toko_olahraga_v2.model.Akun;
import com.app.toko_olahraga_v2.repository.AkunRepository;
import com.app.toko_olahraga_v2.service.AuthService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.dao.DataIntegrityViolationException;

@Controller
@RequestMapping("/kelola-akun")
public class AkunController {

    private final AuthService authService;

    public AkunController(AuthService authService, AkunRepository akunRepository) {
        this.authService = authService;
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
    public String simpan(@ModelAttribute Akun akun, RedirectAttributes redirectAttributes) {
        authService.tambahAkun(akun);
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
    public String update(@ModelAttribute Akun akun, RedirectAttributes redirectAttributes) {
        authService.tambahAkun(akun);
        redirectAttributes.addFlashAttribute("pesan", "Data akun berhasil diperbarui!");
        redirectAttributes.addFlashAttribute("tipePesan", "edit");
        return "redirect:/kelola-akun";
    }
    
    @GetMapping("/hapus/{idAkun}")
    public String hapus(@PathVariable int idAkun, RedirectAttributes redirectAttributes) {
        try {
            authService.hapusAkun(idAkun);
            redirectAttributes.addFlashAttribute("pesan", "Data akun berhasil dihapus!");
            redirectAttributes.addFlashAttribute("tipePesan", "delete");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("pesan", "Gagal menghapus: Akun sedang digunakan!");
            redirectAttributes.addFlashAttribute("tipePesan", "delete");
        }
        return "redirect:/kelola-akun";
    }
}
