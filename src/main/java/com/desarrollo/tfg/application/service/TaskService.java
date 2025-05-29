package com.desarrollo.tfg.application.service;

import com.desarrollo.tfg.application.dto.tasks.TaskRequest;
import com.desarrollo.tfg.application.dto.tasks.TaskResponse;
import com.desarrollo.tfg.domain.entity.PlanWeek;
import com.desarrollo.tfg.domain.entity.Task;
import com.desarrollo.tfg.domain.repository.PlanWeekRepository;
import com.desarrollo.tfg.domain.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@Service
@Transactional
public class TaskService {

  private final TaskRepository taskRepo;
  private final PlanWeekRepository weekRepo;

  public TaskService(TaskRepository taskRepo, PlanWeekRepository weekRepo) {
    this.taskRepo = taskRepo;
    this.weekRepo = weekRepo;
  }

  /** Obtiene todas las tareas de la última semana planificada del usuario */
  public List<TaskResponse> getAll(Long userId) {
    PlanWeek week = weekRepo.findTopByUserIdOrderByWeekStartDesc(userId)
      .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "No hay plan semanal para el usuario"));
    return taskRepo.findAllByPlanWeekIdOrderByDayOfWeekAsc(week.getId())
      .stream()
      .map(this::toDto)
      .collect(Collectors.toList());
  }

  public TaskResponse getById(Long userId, Long taskId) {
    Task t = taskRepo.findById(taskId)
      .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Tarea no encontrada"));
    if (!t.getPlanWeek().getUserId().equals(userId)) {
      throw new ResponseStatusException(FORBIDDEN, "No puedes acceder a esta tarea");
    }
    return toDto(t);
  }

  public TaskResponse create(Long userId, TaskRequest req) {
    PlanWeek week = weekRepo.findTopByUserIdOrderByWeekStartDesc(userId)
      .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "No hay plan semanal para el usuario"));
    Task t = Task.builder()
      .planWeek(week)
      .dayOfWeek(req.getDayOfWeek())
      .description(req.getDescription())
      .type(req.getType())
      .completed(req.getCompleted() != null && req.getCompleted())
      .build();
    return toDto(taskRepo.save(t));
  }

  public TaskResponse update(Long userId, Long taskId, TaskRequest req) {
    Task t = taskRepo.findById(taskId)
      .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Tarea no encontrada"));
    if (!t.getPlanWeek().getUserId().equals(userId)) {
      throw new ResponseStatusException(FORBIDDEN, "No puedes modificar esta tarea");
    }
    t.setDayOfWeek(req.getDayOfWeek());
    t.setDescription(req.getDescription());
    t.setType(req.getType());
    if (req.getCompleted() != null) {
      t.setCompleted(req.getCompleted());
    }
    return toDto(taskRepo.save(t));
  }

  public void delete(Long userId, Long taskId) {
    Task t = taskRepo.findById(taskId)
      .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Tarea no encontrada"));
    if (!t.getPlanWeek().getUserId().equals(userId)) {
      throw new ResponseStatusException(FORBIDDEN, "No puedes borrar esta tarea");
    }
    taskRepo.delete(t);
  }

  private TaskResponse toDto(Task t) {
    return TaskResponse.builder()
      .id(t.getId())
      .dayOfWeek(t.getDayOfWeek())
      .description(t.getDescription())
      .type(t.getType())
      .completed(t.isCompleted())
      .build();
  }
}
