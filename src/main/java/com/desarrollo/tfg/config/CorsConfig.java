package com.desarrollo.tfg.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry
      .addMapping("/**")                   // aplica a todos tus endpoints REST
      .allowedOrigins("http://localhost:5174") // tu frontend
      .allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
      .allowedHeaders("Authorization","Content-Type","*")
      .allowCredentials(true);                 // si usas cookies o auth credentials
  }
}
