package com.desarrollo.tfg.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "habits")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Habit {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long userId;                           // FK al usuario
  private String name;
  private String description;

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private LocalDateTime createdAt = LocalDateTime.now();

  @OneToMany(mappedBy = "habit", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<HabitLog> logs = new ArrayList<>();
}
