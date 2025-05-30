package com.desarrollo.tfg.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "plan_week_id", nullable = true)
  private PlanWeek planWeek;

  @Column(name = "day_of_week", nullable = true)
  private String dayOfWeek;    // Ej: "Lunes", "Martes", ...

  @Column(nullable = true)
  private String description;

  @Column(nullable = true)
  private String type;         // Ej: "HÁBITO", "META", "OTRO"

  @Column(nullable = true)
  private boolean completed;
}
