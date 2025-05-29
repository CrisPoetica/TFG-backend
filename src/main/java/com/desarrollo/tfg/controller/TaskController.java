package com.desarrollo.tfg.controller;

import com.desarrollo.tfg.application.dto.tasks.TaskRequest;
import com.desarrollo.tfg.application.dto.tasks.TaskResponse;
import com.desarrollo.tfg.application.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/{userId}/tasks")
public class TaskController {

  private final TaskService service;

  public TaskController(TaskService service) {
    this.service = service;
  }

  @GetMapping
  public List<TaskResponse> list(@PathVariable Long userId) {
    return service.getAll(userId);
  }

  @GetMapping("/{id}")
  public TaskResponse get(@PathVariable Long userId, @PathVariable Long id) {
    return service.getById(userId, id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public TaskResponse create(@PathVariable Long userId, @RequestBody TaskRequest req) {
    return service.create(userId, req);
  }

  @PutMapping("/{id}")
  public TaskResponse update(
    @PathVariable Long userId,
    @PathVariable Long id,
    @RequestBody TaskRequest req
  ) {
    return service.update(userId, id, req);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long userId, @PathVariable Long id) {
    service.delete(userId, id);
  }
}
