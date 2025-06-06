package com.desarrollo.tfg.controller;

import com.desarrollo.tfg.application.dto.habits.CreateHabitRequest;
import com.desarrollo.tfg.application.dto.habits.HabitLogResponse;
import com.desarrollo.tfg.application.dto.habits.HabitResponse;
import com.desarrollo.tfg.application.dto.habits.LogHabitRequest;
import com.desarrollo.tfg.application.service.HabitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/habits")
@RequiredArgsConstructor
public class HabitController {
  private final HabitService habitService;

  @PostMapping
  public HabitResponse create(@RequestBody  CreateHabitRequest dto) {
    return habitService.createHabit(dto);
  }

  @GetMapping
  public List<HabitResponse> all() {
    return habitService.listHabits();
  }

  @PutMapping("/{id}")
  public HabitResponse update(@PathVariable Long id,
                              @RequestBody  CreateHabitRequest dto) {
    return habitService.updateHabit(id, dto);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    habitService.deleteHabit(id);
  }

  @PostMapping("/{id}/logs")
  public HabitLogResponse log(@PathVariable Long id,
                              @RequestBody  LogHabitRequest dto) {
    return habitService.logHabit(id, dto);
  }

  @GetMapping("/{id}/logs")
  public List<HabitLogResponse> logs(@PathVariable Long id,
                                     @RequestParam LocalDate from,
                                     @RequestParam LocalDate to) {
    return habitService.getLogs(id, from, to);
  }

  @PostMapping("/generate")
  public List<HabitResponse> generate() {
    return habitService.generateHabitsFromAi();
  }
}
