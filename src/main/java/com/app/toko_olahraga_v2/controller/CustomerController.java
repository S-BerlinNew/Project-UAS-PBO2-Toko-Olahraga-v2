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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
@RequestMapping("/customer")
public class CustomerController {

    private CustomerService customerService;

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
    public String index(Model model) {
        model.addAttribute("listCustomer", customerService.getAll());
        model.addAttribute("customer", new Customer());
        model.addAttribute("mode", null);

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
    public String simpan(@ModelAttribute Customer customer) {
        customerService.tambahCustomer(customer);
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
    public String update(@ModelAttribute Customer customer) {
        customerService.tambahCustomer(customer);
        
        return "redirect:/customer";
    }


    @GetMapping("/hapus/{idCustomer}")
    public String hapus(@PathVariable int idCustomer) {
        customerService.hapusCustomer(idCustomer);
        
        return "redirect:/customer";
    }
    
}
