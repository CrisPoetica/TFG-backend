package com.desarrollo.tfg.domain.repository;

import com.desarrollo.tfg.domain.entity.PlanWeek;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface PlanWeekRepository extends JpaRepository<PlanWeek, Long> {
  Optional<PlanWeek> findByUserIdAndWeekStart(Long userId, LocalDate weekStart);
  Optional<PlanWeek> findTopByUserIdOrderByWeekStartDesc(Long userId);

  Optional<PlanWeek> findTopByUserIdAndWeekStartOrderByGeneratedAtDesc(Long userId, LocalDate weekStart);


}
