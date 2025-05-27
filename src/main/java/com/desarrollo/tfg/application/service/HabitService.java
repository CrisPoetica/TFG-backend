package com.desarrollo.tfg.application.service;

import com.desarrollo.tfg.application.dto.habits.*;
import com.desarrollo.tfg.domain.entity.Habit;
import com.desarrollo.tfg.domain.entity.HabitLog;
import com.desarrollo.tfg.domain.repository.HabitLogRepository;
import com.desarrollo.tfg.domain.repository.HabitRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HabitService {
  private final HabitRepository habitRepo;
  private final HabitLogRepository logRepo;
  private final AiService aiService;
  private final UserService userService;

  public HabitResponse createHabit(CreateHabitRequest dto) {
    Habit h = Habit.builder()
      .userId(userService.getCurrentUser().getId())
      .name(dto.getName())
      .description(dto.getDescription())
      .build();
    h = habitRepo.save(h);
    return toResponse(h);
  }

  public List<HabitResponse> listHabits() {
    Long uid = userService.getCurrentUser().getId();
    return habitRepo.findAllByUserIdOrderByCreatedAtDesc(uid)
      .stream().map(this::toResponse).toList();
  }

  public HabitResponse updateHabit(Long id, CreateHabitRequest dto) {
    Habit h = habitRepo.findById(id)
      .filter(x -> x.getUserId().equals(userService.getCurrentUser().getId()))
      .orElseThrow(() -> new RuntimeException("Habit not found"));
    h.setName(dto.getName());
    h.setDescription(dto.getDescription());
    return toResponse(habitRepo.save(h));
  }

  public void deleteHabit(Long id) {
    Habit h = habitRepo.findById(id)
      .filter(x -> x.getUserId().equals(userService.getCurrentUser().getId()))
      .orElseThrow(() -> new RuntimeException("Habit not found"));
    habitRepo.delete(h);
  }

  public HabitLogResponse logHabit(Long habitId, LogHabitRequest dto) {
    Habit h = habitRepo.findById(habitId)
      .filter(x -> x.getUserId().equals(userService.getCurrentUser().getId()))
      .orElseThrow(() -> new RuntimeException("Habit not found"));
    HabitLog log = HabitLog.builder()
      .habit(h)
      .logDate(dto.getLogDate())
      .done(dto.getDone())
      .build();
    log = logRepo.save(log);
    return toLogResponse(log);
  }

  public List<HabitLogResponse> getLogs(Long habitId,
                                        LocalDate from,
                                        LocalDate to) {
    // valida propiedad de usuario…
    return logRepo.findAllByHabitIdAndLogDateBetweenOrderByLogDateAsc(habitId, from, to)
      .stream().map(this::toLogResponse).toList();
  }

  private HabitResponse toResponse(Habit h) {
    return HabitResponse.builder()
      .id(h.getId())
      .name(h.getName())
      .description(h.getDescription())
      .createdAt(h.getCreatedAt())
      .build();
  }

  private HabitLogResponse toLogResponse(HabitLog l) {
    return HabitLogResponse.builder()
      .id(l.getId())
      .logDate(l.getLogDate())
      .done(l.getDone())
      .build();
  }

  @Transactional
  public List<HabitResponse> generateHabitsFromAi() {
    Long userId = userService.getCurrentUser().getId();

    // 1) Montar prompt
    String prompt = """
      Dada la siguiente conversación y perfil de usuario, genera un JSON array de hábitos \
      con campos "name" y "description" para mejorar su productividad y bienestar.
      Devuélvelo estrictamente así:
      [
        { "name": "Beber agua", "description": "..." },
        { "name": "...",    "description": "..." }
      ]
      """;

    // 2) Llamada a la IA
    String aiJson = aiService.ask(prompt);

    // 3) Parsear JSON a List<ParsedHabit>
    List<ParsedHabit> parsed;
    try {
      parsed = new ObjectMapper()
        .readValue(aiJson, new TypeReference<List<ParsedHabit>>() {});
    } catch (Exception ex) {
      throw new RuntimeException("Error parseando IA: " + ex.getMessage(), ex);
    }

    // 4) Guardar cada hábito
    List<HabitResponse> result = new ArrayList<>();
    for (ParsedHabit ph : parsed) {
      Habit h = Habit.builder()
        .userId(userId)
        .name(ph.getName())
        .description(ph.getDescription())
        .build();
      h = habitRepo.save(h);
      result.add(HabitResponse.builder()
        .id(h.getId())
        .name(h.getName())
        .description(h.getDescription())
        .createdAt(h.getCreatedAt())
        .build());
    }
    return result;
  }
}
