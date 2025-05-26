package com.desarrollo.tfg.config;

import com.desarrollo.tfg.application.jwt.JwtAuthenticationFilter;
import com.desarrollo.tfg.domain.repository.UserRepository;
import com.desarrollo.tfg.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtUtil jwtUtil;
  private final AuthenticationConfiguration authConfig;

  // 1) Password encoder
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  // 2) Carga usuarios desde la BD (vuelve a exponer este bean)
  @Bean
  public UserDetailsService userDetailsService(UserRepository userRepo) {
    return username -> userRepo.findByUsername(username)
      .map(dbUser -> User.withUsername(dbUser.getUsername())
        .password(dbUser.getPassword())
        .authorities(dbUser.getRoles().split(","))
        .build()
      )
      .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
  }

  // 3) AuthenticationManager automático
  @Bean
  public AuthenticationManager authenticationManager() throws Exception {
    return authConfig.getAuthenticationManager();
  }

  // 4) Filtro JWT que valida token en cada petición
  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter(UserDetailsService uds) {
    return new JwtAuthenticationFilter(jwtUtil, uds);
  }

  // 5) Cadena de seguridad: sin estado, JWT en header, permite register/login
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                 JwtAuthenticationFilter jwtFilter) throws Exception {
    http
      .csrf(csrf -> csrf.disable())
      .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authorizeHttpRequests(authz -> authz
        .requestMatchers("/api/v1/auth/register", "/api/v1/auth/login", "/h2-console/**")
        .permitAll()
        .anyRequest().authenticated()
      )
      .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
      .logout(logout -> logout
        .logoutUrl("/api/v1/auth/logout")
        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT))
      )
      .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

    return http.build();
  }
}

