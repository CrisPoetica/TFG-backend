package com.desarrollo.tfg.domain.repository;

import com.desarrollo.tfg.domain.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
  List<Task> findAllByPlanWeekIdOrderByDayOfWeekAsc(Long planWeekId);
}
