package com.desarrollo.tfg.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class OpenAiConfig {

  @Value("${openai.api-base}")
  private String apiBase;

  @Bean
  public WebClient openAiClient(@Value("${openai.api-key}") String apiKey) {
    return WebClient.builder()
      .baseUrl(apiBase)
      .defaultHeader("Authorization", "Bearer " + apiKey)
      .build();
  }
}
