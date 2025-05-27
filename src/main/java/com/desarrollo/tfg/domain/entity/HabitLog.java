package com.desarrollo.tfg.domain.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Entity
@Table(name = "habit_logs",
  uniqueConstraints = @UniqueConstraint(columnNames = {"habit_id", "log_date"}))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HabitLog {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne @JoinColumn(name = "habit_id", nullable = false)
  private Habit habit;

  @Column(name = "log_date", nullable = false)
  private LocalDate logDate;

  @Column(nullable = false)
  private Boolean done = false;
}
