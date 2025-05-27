package com.desarrollo.tfg.domain.repository;

import com.desarrollo.tfg.domain.entity.HabitLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface HabitLogRepository extends JpaRepository<HabitLog, Long> {
  List<HabitLog> findAllByHabitIdAndLogDateBetweenOrderByLogDateAsc(Long habitId,
                                                                    LocalDate from,
                                                                    LocalDate to);
}
