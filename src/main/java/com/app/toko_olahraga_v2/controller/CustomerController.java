package com.app.toko_olahraga_v2.controller;

import com.app.toko_olahraga_v2.model.Customer;
import com.app.toko_olahraga_v2.model.Akun;
import com.app.toko_olahraga_v2.model.LogAktivitas;
import com.app.toko_olahraga_v2.service.CustomerService;
import com.app.toko_olahraga_v2.service.LogAktivitasService;
import com.app.toko_olahraga_v2.service.AuthService;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.dao.DataIntegrityViolationException;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;
    private final LogAktivitasService logAktivitasService;
    private final AuthService authService;

    public CustomerController(CustomerService customerService, LogAktivitasService logAktivitasService, AuthService authService) {
        this.customerService = customerService;
        this.logAktivitasService = logAktivitasService;
        this.authService = authService;
    }

    @PostMapping("/simpan-ajax")
    @ResponseBody
    public ResponseEntity<?> simpanAjax(@RequestBody Customer customer, HttpSession session) {
        customerService.tambahCustomer(customer); 

        try {
            Integer idAkun = (Integer) session.getAttribute("idAkun");
            Akun akun = (idAkun != null) ? authService.getAkunById(idAkun) : null;
            LogAktivitas log = new LogAktivitas();
            log.setAkun(akun);
            log.setAksi("Menambahkan customer baru (AJAX): " + customer.getNamaCustomer() + " (No. Telp: " + customer.getNoTelepon() + ")");
            logAktivitasService.addLog(log);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return ResponseEntity.ok(customer); 
    }
    
    @GetMapping
    public String index(
            @org.springframework.web.bind.annotation.RequestParam(required = false) String keyword,
            @org.springframework.web.bind.annotation.RequestParam(required = false, defaultValue = "desc") String sort,
            Model model) {
        
        model.addAttribute("listCustomer", customerService.getAll(keyword, sort));
        model.addAttribute("customer", new Customer());
        model.addAttribute("mode", null);
        
        // Return back to view to keep the form state
        model.addAttribute("keyword", keyword);
        model.addAttribute("sort", sort);

        return "customer/index";
    }

    @GetMapping("/tambah")
    public String tambahForm(Model model) {
        model.addAttribute("listCustomer", customerService.getAll());
        model.addAttribute("customer", new Customer());
        model.addAttribute("mode", "tambah");

        return "customer/index";
    }

    @PostMapping("/simpan")
    public String simpan(@ModelAttribute Customer customer, RedirectAttributes redirectAttributes, HttpSession session) {
        customerService.tambahCustomer(customer);

        try {
            Integer idAkun = (Integer) session.getAttribute("idAkun");
            Akun akun = (idAkun != null) ? authService.getAkunById(idAkun) : null;
            LogAktivitas log = new LogAktivitas();
            log.setAkun(akun);
            log.setAksi("Menambahkan customer baru: " + customer.getNamaCustomer() + " (No. Telp: " + customer.getNoTelepon() + ")");
            logAktivitasService.addLog(log);
        } catch (Exception e) {
            e.printStackTrace();
        }

        redirectAttributes.addFlashAttribute("pesan", "Data customer berhasil ditambahkan!");
        redirectAttributes.addFlashAttribute("tipePesan", "success");
        return "redirect:/customer";
    }

    @GetMapping("/edit/{idCustomer}")
    public String editForm(
        @PathVariable int idCustomer,
        Model model) {
            model.addAttribute("listCustomer", customerService.getAll());
            model.addAttribute("customer", customerService.getCustomerById(idCustomer));
            model.addAttribute("mode", "edit");
            
            return "customer/index";
        }

    @PostMapping("/update")
    public String update(@ModelAttribute Customer customer, RedirectAttributes redirectAttributes, HttpSession session) {
        customerService.tambahCustomer(customer);

        try {
            Integer idAkun = (Integer) session.getAttribute("idAkun");
            Akun akun = (idAkun != null) ? authService.getAkunById(idAkun) : null;
            LogAktivitas log = new LogAktivitas();
            log.setAkun(akun);
            log.setAksi("Mengedit customer: " + customer.getNamaCustomer() + " (No. Telp: " + customer.getNoTelepon() + ")");
            logAktivitasService.addLog(log);
        } catch (Exception e) {
            e.printStackTrace();
        }

        redirectAttributes.addFlashAttribute("pesan", "Data customer berhasil diperbarui!");
        redirectAttributes.addFlashAttribute("tipePesan", "edit");
        return "redirect:/customer";
    }

    @GetMapping("/hapus/{idCustomer}")
    public String hapus(@PathVariable int idCustomer, RedirectAttributes redirectAttributes, HttpSession session) {
        try {
            Customer customer = customerService.getCustomerById(idCustomer);
            String namaCustomer = (customer != null) ? customer.getNamaCustomer() : "Tidak Diketahui";

            customerService.hapusCustomer(idCustomer);

            try {
                Integer idAkun = (Integer) session.getAttribute("idAkun");
                Akun akun = (idAkun != null) ? authService.getAkunById(idAkun) : null;
                LogAktivitas log = new LogAktivitas();
                log.setAkun(akun);
                log.setAksi("Menghapus customer: " + namaCustomer + " (ID: " + idCustomer + ")");
                logAktivitasService.addLog(log);
            } catch (Exception logEx) {
                logEx.printStackTrace();
            }

            redirectAttributes.addFlashAttribute("pesan", "Data customer berhasil dihapus!");
            redirectAttributes.addFlashAttribute("tipePesan", "delete");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("pesan", "Gagal menghapus: Customer sudah ada di transaksi!");
            redirectAttributes.addFlashAttribute("tipePesan", "delete");
        }
        return "redirect:/customer";
    }
    
}
