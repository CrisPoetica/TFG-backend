package com.desarrollo.tfg.application.dto.journal;

import lombok.Data;

import java.time.LocalDate;

@Data
public class JournalEntryRequest {
  private LocalDate entryDate;
  private String content;
}
