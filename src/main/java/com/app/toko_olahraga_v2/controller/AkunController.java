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


@Controller
@RequestMapping("/kelola-akun")
public class AkunController {

    private final AuthService authService;

    public AkunController(AuthService authService, AkunRepository akunRepository) {
        this.authService = authService;
    }

    
    @GetMapping()
    public String kelolaAkunPage(Model model) {
        model.addAttribute("listAkun", authService.getAll());
        model.addAttribute("akun", new Akun()); 
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
    public String simpan(@ModelAttribute Akun akun) {
        authService.tambahAkun(akun);
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
    public String update(@ModelAttribute Akun akun) {
        authService.tambahAkun(akun);
        return "redirect:/kelola-akun";
    }
    
    @GetMapping("/hapus/{idAkun}")
    public String hapus(@PathVariable int idAkun) {
        authService.hapusAkun(idAkun);
        return "redirect:/kelola-akun";
    }
}
