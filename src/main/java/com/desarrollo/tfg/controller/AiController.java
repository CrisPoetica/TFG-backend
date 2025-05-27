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
  @ResponseStatus(HttpStatus.CREATED)
  public ConversationResponse startConversation() {
    UserResponse current = userService.getCurrentUser();
    Conversation conv = Conversation.builder()
      .userId(current.getId())
      .startedAt(LocalDateTime.now())
      .build();
    conv = convRepo.save(conv);
    return ConversationResponse.builder()
      .id(conv.getId())
      .startedAt(conv.getStartedAt())
      .messages(List.of())
      .build();
  }

  @GetMapping("/conversations/{id}")
  public ConversationResponse getConversation(@PathVariable Long id) {
    UserResponse current = userService.getCurrentUser();
    Conversation conv = convRepo.findByIdAndUserId(id, current.getId())
      .orElseThrow(() -> new RuntimeException("Conversation no encontrada o no autorizada"));

    List<MessageResponse> history = msgRepo.findAllByConversationIdOrderBySentAtAsc(id)
      .stream()
      .map(m -> MessageResponse.builder()
        .id(m.getId())
        .sender(m.getSender())
        .content(m.getContent())
        .sentAt(m.getSentAt())
        .build())
      .collect(Collectors.toList());

    return ConversationResponse.builder()
      .id(conv.getId())
      .startedAt(conv.getStartedAt())
      .messages(history)
      .build();
  }

  @PostMapping("/conversations/{id}/messages")
  @ResponseStatus(HttpStatus.OK)
  public MessageResponse sendMessage(
    @PathVariable Long id,
    @RequestBody MessageRequest req) {
    UserResponse current = userService.getCurrentUser();
    Conversation conv = convRepo.findByIdAndUserId(id, current.getId())
      .orElseThrow(() -> new RuntimeException("Conversation no encontrada o no autorizada"));

    Message userMsg = Message.builder()
      .conversation(conv)
      .sender("USER")
      .content(req.getContent())
      .sentAt(LocalDateTime.now())
      .build();
    msgRepo.save(userMsg);

    String aiReply = aiService.ask(req.getContent());

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
