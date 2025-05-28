package com.desarrollo.tfg.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "journal_entries")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JournalEntry {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "entry_date", nullable = false)
  private LocalDate entryDate;

  @Lob
  @Column(nullable = false)
  private String content;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @PrePersist
  public void prePersist() {
    if (this.entryDate == null) {
      this.entryDate = LocalDate.now();
    }
  }

  @PreUpdate
  public void preUpdate() {
    if (this.entryDate == null) {
      this.entryDate = LocalDate.now();
    }
  }
}
