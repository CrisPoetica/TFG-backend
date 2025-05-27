package com.desarrollo.tfg.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Profile("prod")
public class OpenAiService implements AiService {
  private final WebClient openAiClient;

  @Override
  public String ask(String prompt) {
    // Construimos el payload
    Map<String, Object> body = Map.of(
      "model", "gpt-4o-mini",
      "store", true,
      "messages", List.of(Map.of("role", "user", "content", prompt))
    );

    // Llamada síncrona (bloqueante) para simplificar
    Mono<Map> responseMono = openAiClient
      .post()
      .uri("/chat/completions")
      .bodyValue(body)
      .retrieve()
      .bodyToMono(Map.class);

    Map<?,?> resp = responseMono.block();
    // El JSON viene con { choices: [ { message: { content: "..."} } ] }
    List<?> choices = (List<?>) resp.get("choices");
    if (choices != null && !choices.isEmpty()) {
      Map<?,?> first = (Map<?,?>) choices.get(0);
      Map<?,?> message = (Map<?,?>) first.get("message");
      return (String) message.get("content");
    }
    throw new RuntimeException("OpenAI no devolvió respuesta válida");
  }
}
