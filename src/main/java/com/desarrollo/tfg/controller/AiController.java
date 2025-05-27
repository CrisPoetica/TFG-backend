package com.desarrollo.tfg.controller;

import com.desarrollo.tfg.application.dto.*;
import com.desarrollo.tfg.application.service.AiService;
import com.desarrollo.tfg.application.service.UserService;
import com.desarrollo.tfg.domain.entity.*;
import com.desarrollo.tfg.domain.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AiController {
  private final ConversationRepository convRepo;
  private final MessageRepository msgRepo;
  private final AiService aiService;
  private final UserService userService;

  @PostMapping("/conversations")
  public ConversationResponse startConversation(Authentication auth) {

    UserResponse current = userService.getCurrentUser();
    Long userId = current.getId();

    Conversation conv = Conversation.builder()
      .userId(userId)
      .startedAt(LocalDateTime.now())
      .build();
    conv = convRepo.save(conv);
    return ConversationResponse.builder()
      .id(conv.getId())
      .startedAt(conv.getStartedAt())
      .messages(List.of())
      .build();
  }

  @PostMapping("/conversations/{id}/messages")
  @ResponseStatus(HttpStatus.OK)
  public MessageResponse sendMessage(@PathVariable Long id, @RequestBody MessageRequest req, Authentication auth) {
    // Verificar propiedad de la conversación usando UserService
    UserResponse current = userService.getCurrentUser();
    Long userId = current.getId();

    Conversation conv = convRepo.findByIdAndUserId(id, userId)
      .orElseThrow(() -> new RuntimeException("Conversation no encontrada o no autorizada"));

    // Guardar mensaje de usuario
    Message userMsg = Message.builder()
      .conversation(conv)
      .sender("USER")
      .content(req.getContent())
      .sentAt(LocalDateTime.now())
      .build();
    msgRepo.save(userMsg);

    // Obtener respuesta (mock)
    String aiReply = aiService.ask(req.getContent());

    // Guardar mensaje de IA
    Message aiMsg = Message.builder()
      .conversation(conv)
      .sender("AI")
      .content(aiReply)
      .sentAt(LocalDateTime.now())
      .build();
    msgRepo.save(aiMsg);

    return MessageResponse.builder()
      .id(aiMsg.getId())
      .sender(aiMsg.getSender())
      .content(aiMsg.getContent())
      .sentAt(aiMsg.getSentAt())
      .build();
  }
}
