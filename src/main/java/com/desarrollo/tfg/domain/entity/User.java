package com.desarrollo.tfg.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(unique=true, nullable=false)
  String username;

  @Column(nullable=false)
  String password;           // bcrypt-hash

  @Column(unique=true)
  String email;

  @Column(nullable=false)
  String roles;              // e.g. "ROLE_USER"

  @Builder.Default
  @Column(nullable = false)
  private Boolean firstLogin = true;

  @CreationTimestamp
  LocalDateTime createdAt;
}
