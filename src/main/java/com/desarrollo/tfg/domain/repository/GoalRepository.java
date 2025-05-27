package com.desarrollo.tfg.domain.repository;

import com.desarrollo.tfg.domain.entity.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GoalRepository extends JpaRepository<Goal, Long> {
  List<Goal> findAllByUserId(Long userId);
}
