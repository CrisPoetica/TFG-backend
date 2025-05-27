package com.desarrollo.tfg.controller;

import com.desarrollo.tfg.application.dto.weekplanner.CreatePlanRequest;
import com.desarrollo.tfg.application.dto.weekplanner.PlanResponse;
import com.desarrollo.tfg.application.dto.weekplanner.TaskResponse;
import com.desarrollo.tfg.application.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/plans")
@RequiredArgsConstructor
public class PlanController {
  private final PlanService planService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public PlanResponse create(@RequestBody CreatePlanRequest req) {
    return planService.createPlan(req.getWeekStart());
  }

  @GetMapping("/current")
  public PlanResponse current() {
    return planService.getCurrentPlan();
  }

  @GetMapping("/{planId}/tasks")
  public List<TaskResponse> listTasks(@PathVariable Long planId) {
    return planService.getCurrentPlan().getTasks(); // o un método específico
  }

  @PatchMapping("/{planId}/tasks/{taskId}")
  public TaskResponse toggleTask(
    @PathVariable Long planId,
    @PathVariable Long taskId,
    @RequestBody Map<String,Boolean> body) {
    return planService.updateTask(planId, taskId, body.get("completed"));
  }
}
