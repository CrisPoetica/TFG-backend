package com.desarrollo.tfg.application.service;

import com.desarrollo.tfg.application.dto.goals.ParsedGoal;
import com.desarrollo.tfg.application.dto.goals.GoalResponse;
import com.desarrollo.tfg.domain.entity.Conversation;
import com.desarrollo.tfg.domain.entity.Message;
import com.desarrollo.tfg.domain.entity.Goal;
import com.desarrollo.tfg.domain.repository.ConversationRepository;
import com.desarrollo.tfg.domain.repository.MessageRepository;
import com.desarrollo.tfg.domain.repository.GoalRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoalService {
  private final GoalRepository goalRepo;
  private final ConversationRepository convRepo;
  private final MessageRepository msgRepo;
  private final AiService aiService;
  private final UserService userService;

  @Transactional
  public List<GoalResponse> generateGoals() {
    Long userId = userService.getCurrentUser().getId();

    // 1) Prepara el prompt a partir del contexto
    String prompt = buildGoalsPrompt(userId);

    // 2) Llama a la IA
    String aiOutput = aiService.ask(prompt);

    // 3) Extrae y parsea sólo la parte JSON
    List<ParsedGoal> parsed = parseAiGoals(aiOutput);

    // 4) Persiste y mapea a DTO
    List<GoalResponse> responses = new ArrayList<>();
    for (ParsedGoal pg : parsed) {
      Goal g = Goal.builder()
        .userId(userId)
        .title(pg.getTitle())
        .description(pg.getDescription())
        .status(pg.getStatus())
        .build();
      goalRepo.save(g);
      responses.add(GoalResponse.builder()
        .id(g.getId())
        .title(g.getTitle())
        .description(g.getDescription())
        .status(g.getStatus())
        .build());
    }

    return responses;
  }

  public List<GoalResponse> listGoals() {
    Long userId = userService.getCurrentUser().getId();
    return goalRepo.findAllByUserId(userId)
      .stream()
      .map(GoalResponse::fromEntity)
      .toList();
  }

  private String buildGoalsPrompt(Long userId) {
    Conversation conv = convRepo
      .findTopByUserIdOrderByStartedAtDesc(userId)
      .orElseThrow(() -> new RuntimeException("No hay conversación previa"));
    List<Message> history = msgRepo
      .findAllByConversationIdOrderBySentAtAsc(conv.getId());

    StringBuilder sb = new StringBuilder("Contexto de la conversación:\n");
    history.forEach(m ->
      sb.append(m.getSender()).append(": ").append(m.getContent()).append("\n")
    );
    sb.append("\nA partir de este contexto, genera **solo** un array JSON con 3–5 objetivos SMART ")
      .append("con campos \"title\",\"description\",\"status\" en formato:\n")
      .append("[{\"title\":\"...\",\"description\":\"...\",\"status\":\"PENDING\"},…]");
    return sb.toString();
  }

  /**
   * Extrae del texto de la IA únicamente la parte JSON (entre el primer [ y el último ])
   * y la parsea a List<ParsedGoal>.
   */
  private List<ParsedGoal> parseAiGoals(String aiOutput) {
    try {
      int start = aiOutput.indexOf('[');
      int end   = aiOutput.lastIndexOf(']');
      if (start < 0 || end < 0 || end <= start) {
        throw new RuntimeException("Respuesta de IA sin array JSON válido");
      }
      String justJson = aiOutput.substring(start, end + 1);
      ObjectMapper mapper = new ObjectMapper();
      return mapper.readValue(
        justJson,
        new TypeReference<List<ParsedGoal>>() {}
      );
    } catch (Exception ex) {
      throw new RuntimeException("Error parseando JSON de Goals IA", ex);
    }
  }

  /**
   * Recupera todos los goals del usuario actual y los convierte a DTO.
   */
  public List<GoalResponse> getAllUserGoals() {
    Long userId = userService.getCurrentUser().getId();
    List<Goal> goals = goalRepo.findAllByUserId(userId);
    return goals.stream()
      .map(g -> GoalResponse.builder()
        .id(g.getId())
        .title(g.getTitle())
        .description(g.getDescription())
        .status(g.getStatus())
        .build()
      )
      .toList();
  }
}
