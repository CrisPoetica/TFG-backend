package com.desarrollo.tfg.application.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationResponse {
  private Long id;
  private LocalDateTime startedAt;
  private List<MessageResponse> messages;
}
