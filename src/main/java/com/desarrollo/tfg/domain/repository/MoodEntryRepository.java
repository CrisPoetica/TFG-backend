package com.desarrollo.tfg.domain.repository;

import com.desarrollo.tfg.domain.entity.MoodEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MoodEntryRepository extends JpaRepository<MoodEntry, Long> {
  List<MoodEntry> findAllByUserIdAndEntryDateBetweenOrderByEntryDate(
    Long userId, LocalDate start, LocalDate end);

  Optional<MoodEntry> findByUserIdAndEntryDate(Long userId, LocalDate date);
}
