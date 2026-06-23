package com.app.toko_olahraga_v2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    public WebConfig(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**") //Jaga ketat SEMUA URL rute di aplikasi tanpa terkecuali
                .excludePathPatterns(
                    //KECUALI URL link ini
                    "/",          
                    "/login",     
                    "/css/**"   
                );
    }
}