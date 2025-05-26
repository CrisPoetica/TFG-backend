package com.desarrollo.tfg.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
  @Value("${jwt.secret}")
  private String secretKey;

  @Value("${jwt.expiration}")
  private long validityMs;

  private Key getSigningKey() {
    return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
  }

  public String generateToken(UserDetails userDetails) {
    Date now = new Date();
    Date expiry = new Date(now.getTime() + validityMs);
    return Jwts.builder()
      .setSubject(userDetails.getUsername())
      .setIssuedAt(now)
      .setExpiration(expiry)
      .signWith(getSigningKey(), SignatureAlgorithm.HS256)
      .compact();
  }

  public String extractUsername(String token) {
    return Jwts.parserBuilder()
      .setSigningKey(getSigningKey())
      .build()
      .parseClaimsJws(token)
      .getBody()
      .getSubject();
  }

  public boolean validateToken(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return username.equals(userDetails.getUsername()) &&
      !Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getExpiration()
        .before(new Date());
  }
}

