package com.desarrollo.tfg.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "plan_weeks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanWeek {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "week_start", nullable = false)
  private LocalDate weekStart;

  @Column(name = "generated_at", nullable = false)
  private LocalDateTime generatedAt;

  @OneToMany(mappedBy = "planWeek", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Task> tasks = new ArrayList<>();
}
