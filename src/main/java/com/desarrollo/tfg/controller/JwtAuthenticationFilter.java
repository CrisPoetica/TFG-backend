/*
package com.desarrollo.tfg.controller;

import com.desarrollo.tfg.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JwtUtil jwtUtil;
  private final UserDetailsService userDetailsService;

  public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService uds) {
    this.jwtUtil = jwtUtil;
    this.userDetailsService = uds;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
    throws ServletException, IOException {
    String authHeader = req.getHeader("Authorization");
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      String token = authHeader.substring(7);
      String username = jwtUtil.extractUsername(token);
      if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (jwtUtil.validateToken(token, userDetails)) {
          UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(
              userDetails, null, userDetails.getAuthorities()
            );
          auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
          SecurityContextHolder.getContext().setAuthentication(auth);
        }
      }
    }
    chain.doFilter(req, res);
  }

}

*/
