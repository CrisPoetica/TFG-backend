package com.desarrollo.tfg.domain.repository;

import com.desarrollo.tfg.domain.entity.Habit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HabitRepository extends JpaRepository<Habit, Long> {
  List<Habit> findAllByUserIdOrderByCreatedAtDesc(Long userId);
}
