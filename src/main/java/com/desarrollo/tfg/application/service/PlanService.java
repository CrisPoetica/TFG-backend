package com.desarrollo.tfg.application.service;

import com.desarrollo.tfg.application.dto.weekplanner.ParsedTask;
import com.desarrollo.tfg.application.dto.weekplanner.PlanResponse;
import com.desarrollo.tfg.application.dto.weekplanner.TaskResponse;
import com.desarrollo.tfg.domain.entity.Conversation;
import com.desarrollo.tfg.domain.entity.Message;
import com.desarrollo.tfg.domain.entity.PlanWeek;
import com.desarrollo.tfg.domain.entity.Task;
import com.desarrollo.tfg.domain.repository.ConversationRepository;
import com.desarrollo.tfg.domain.repository.MessageRepository;
import com.desarrollo.tfg.domain.repository.PlanWeekRepository;
import com.desarrollo.tfg.domain.repository.TaskRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanService {
  private final PlanWeekRepository planRepo;
  private final TaskRepository taskRepo;
  private final AiService aiService;
  private final UserService userService;
  private final ConversationRepository convRepo;
  private final MessageRepository msgRepo;

  @Transactional
  public PlanResponse createPlan(LocalDate weekStart) {
    Long userId = userService.getCurrentUser().getId();

    // 1) Verificar no duplicar
    if (planRepo.findByUserIdAndWeekStart(userId, weekStart).isPresent()) {
      throw new RuntimeException("Ya existe un plan para esa semana");
    }

    // 2) Invocar IA: pasamos el contexto
    String prompt = buildWeeklyPrompt(userId, weekStart);
    String aiOutput = aiService.ask(prompt);
    // 3) Parsear IA → List<ParsedTask>
    List<ParsedTask> parsed = parseAiTasks(aiOutput);

    // 4) Crear entidad PlanWeek
    PlanWeek plan = PlanWeek.builder()
      .userId(userId)
      .weekStart(weekStart)
      .generatedAt(LocalDateTime.now())
      .build();
    plan = planRepo.save(plan);

    // 5) Persistir cada tarea
    List<TaskResponse> responses = new ArrayList<>();
    for (ParsedTask p : parsed) {
      Task t = Task.builder()
        .planWeek(plan)
        .dayOfWeek(p.getDay())
        .description(p.getDescription())
        .type(p.getType())
        .build();
      t = taskRepo.save(t);
      responses.add(TaskResponse.builder()
        .id(t.getId())
        .dayOfWeek(t.getDayOfWeek())
        .description(t.getDescription())
        .type(t.getType())
        .completed(false)
        .build());
    }

    return PlanResponse.builder()
      .id(plan.getId())
      .weekStart(plan.getWeekStart())
      .generatedAt(plan.getGeneratedAt())
      .tasks(responses)
      .build();
  }

  public PlanResponse getCurrentPlan() {
    Long userId = userService.getCurrentUser().getId();
    LocalDate today = LocalDate.now();
    LocalDate thisMonday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    PlanWeek plan = planRepo.findByUserIdAndWeekStart(userId, thisMonday)
      .orElseThrow(() -> new RuntimeException("No hay plan para la semana actual"));

    List<TaskResponse> tasks = taskRepo
      .findAllByPlanWeekIdOrderByDayOfWeekAsc(plan.getId())
      .stream()
      .map(t -> TaskResponse.builder()
        .id(t.getId())
        .dayOfWeek(t.getDayOfWeek())
        .description(t.getDescription())
        .type(t.getType())
        .completed(false)
        .build()).toList();

    return PlanResponse.builder()
      .id(plan.getId())
      .weekStart(plan.getWeekStart())
      .generatedAt(plan.getGeneratedAt())
      .tasks(tasks)
      .build();
  }

  @Transactional
  public TaskResponse updateTask(Long planId, Long taskId, boolean completed) {
    // verificar propiedad
    PlanWeek plan = planRepo.findById(planId)
      .filter(p -> p.getUserId().equals(userService.getCurrentUser().getId()))
      .orElseThrow(() -> new RuntimeException("Plan no encontrado o no autorizado"));
    Task t = taskRepo.findById(taskId)
      .filter(x -> x.getPlanWeek().getId().equals(planId))
      .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
    t.setCompleted(completed);
    t = taskRepo.save(t);
    return TaskResponse.builder()
      .id(t.getId())
      .dayOfWeek(t.getDayOfWeek())
      .description(t.getDescription())
      .type(t.getType())
      .completed(false)
      .build();
  }

  /** Construye el prompt para pedirle a la IA que genere tareas semanales */
  private String buildWeeklyPrompt(Long userId, LocalDate weekStart) {
    // 1) Recupera la última conversación
    Conversation conv = convRepo
      .findTopByUserIdOrderByStartedAtDesc(userId)
      .orElseThrow(() -> new RuntimeException("No hay conversación previa"));

    // 2) Recupera todos los mensajes de esa conversación
    List<Message> history = msgRepo
      .findAllByConversationIdOrderBySentAtAsc(conv.getId());

    // 3) Monta el texto
    StringBuilder sb = new StringBuilder();
    sb.append("Contexto de la conversación:\n");
    for (Message m : history) {
      sb.append(m.getSender())
        .append(": ")
        .append(m.getContent())
        .append("\n");
    }
    sb.append("\nGenera **únicamente** un JSON válido con un array de objetos para un plan semanal ")
      .append("para la semana que empieza el ").append(weekStart).append(". ")
      .append("Cada objeto debe tener EXACTAMENTE las claves:\n")
      .append("  • \"day\" (por ejemplo: \"Lunes\")\n")
      .append("  • \"description\"\n")
      .append("  • \"type\" (\"HÁBITO\", \"META\" u \"OTRO\").\n")
      .append("**No incluyas ningún texto, explicación o saludo**—solo el array JSON.")
      .append("**Importante**: devuélveme **solo** un array JSON, sin ``` ni explicaciones ni cabeceras ni pies de página.");

    return sb.toString();
  }


  private List<ParsedTask> parseAiTasks(String aiOutput) {
    try {
      // 1) Elimina code fences (```json …```)
      String cleaned = aiOutput.replaceAll("```+\\s*", "");
      // 2) Busca los corchetes
      int start = cleaned.indexOf('[');
      int end   = cleaned.lastIndexOf(']');
      if (start < 0 || end < 0 || end <= start) {
        throw new RuntimeException("No pude encontrar un array JSON en la salida de la IA");
      }
      String jsonOnly = cleaned.substring(start, end + 1);

      // 3) Lee el JSON puro
      ObjectMapper mapper = new ObjectMapper();
      return mapper.readValue(
        jsonOnly,
        new TypeReference<List<ParsedTask>>() {}
      );
    } catch (Exception ex) {
      throw new RuntimeException("Error parseando tareas IA", ex);
    }
  }

}
