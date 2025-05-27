package com.desarrollo.tfg.application.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageResponse {
  private Long id;
  private String sender;
  private String content;
  private LocalDateTime sentAt;
}
