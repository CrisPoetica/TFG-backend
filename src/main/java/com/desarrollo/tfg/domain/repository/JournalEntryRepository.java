package com.desarrollo.tfg.domain.repository;

import com.desarrollo.tfg.domain.entity.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {
  List<JournalEntry> findAllByUserIdOrderByEntryDateDesc(Long userId);
  Optional<JournalEntry> findByIdAndUserId(Long id, Long userId);
}
