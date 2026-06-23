package com.app.toko_olahraga_v2.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        
        // ========CEK USER SUDAH LOGIN/SEDANG LOGIN=======
        if (session.getAttribute("loggedInUser") == null) {
            response.sendRedirect(request.getContextPath() + "/");
            return false; 
        }

        //========ROLE UNTUK USERSESSION=========
        String role = (String) session.getAttribute("role");
        
        //========URL YANG USER AKSES========
        String currentUrl = request.getRequestURI();

        //========BLOKIR/MENYEDIAKAN FITUR SESUAI ROLE============
        if("KASIR".equals(role)) {
            if(currentUrl.contains("/kelola-akun") || currentUrl.contains("/laporan") || currentUrl.contains("/barang")) {
                session.setAttribute("errorAkses", "Maaf, Terjadi Kesalahan!");
                response.sendRedirect(request.getContextPath() + "/dashboard");
                return false;
            }
        }

        return true; // TRUE = Lolos pemeriksaan, silakan lanjut ke Controller tujuan
    }
}
