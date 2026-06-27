package com.app.toko_olahraga_v2.controller;

import com.app.toko_olahraga_v2.model.Customer;
import com.app.toko_olahraga_v2.service.CustomerService;

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

@Controller
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/simpan-ajax") // Pastikan url-nya sama persis lowercase
    @ResponseBody
    public ResponseEntity<?> simpanAjax(@RequestBody Customer customer) {
        // Tinggal panggil fungsi service lo yang sekarang sudah pinter generate kode otomatis
        customerService.tambahCustomer(customer); 
        
        // Kembalikan objek customer yang sudah komplit dengan kodenya ke JavaScript
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
    public String simpan(@ModelAttribute Customer customer, RedirectAttributes redirectAttributes) {
        customerService.tambahCustomer(customer);
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
    public String update(@ModelAttribute Customer customer, RedirectAttributes redirectAttributes) {
        customerService.tambahCustomer(customer);
        redirectAttributes.addFlashAttribute("pesan", "Data customer berhasil diperbarui!");
        redirectAttributes.addFlashAttribute("tipePesan", "edit");
        return "redirect:/customer";
    }

    @GetMapping("/hapus/{idCustomer}")
    public String hapus(@PathVariable int idCustomer, RedirectAttributes redirectAttributes) {
        try {
            customerService.hapusCustomer(idCustomer);
            redirectAttributes.addFlashAttribute("pesan", "Data customer berhasil dihapus!");
            redirectAttributes.addFlashAttribute("tipePesan", "delete");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("pesan", "Gagal menghapus: Customer sudah ada di transaksi!");
            redirectAttributes.addFlashAttribute("tipePesan", "delete");
        }
        return "redirect:/customer";
    }
    
}
