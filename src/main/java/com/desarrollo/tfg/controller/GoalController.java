package com.desarrollo.tfg.controller;

import com.desarrollo.tfg.application.dto.goals.GoalResponse;
import com.desarrollo.tfg.application.service.GoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/goals")
@RequiredArgsConstructor
public class GoalController {
  private final GoalService goalService;

  /**
   * Genera nuevos goals a partir del contexto de la última conversación
   * llamando a la IA, los persiste y devuelve la lista.
   */
  @PostMapping("/generate")
  public ResponseEntity<List<GoalResponse>> generateGoals() {
    List<GoalResponse> responses = goalService.generateGoals();
    return ResponseEntity.ok(responses);
  }

  /**
   * (Opcional) Recupera todos los goals creados por el usuario.
   */
  @GetMapping
  public ResponseEntity<List<GoalResponse>> listGoals() {
    List<GoalResponse> responses = goalService.getAllUserGoals();
    return ResponseEntity.ok(responses);
  }
}
