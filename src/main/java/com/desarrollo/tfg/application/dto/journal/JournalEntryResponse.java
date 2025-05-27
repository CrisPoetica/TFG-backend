package com.desarrollo.tfg.application.dto.journal;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class JournalEntryResponse {
  private Long id;
  private LocalDate entryDate;
  private String content;
  private LocalDateTime createdAt;
}
