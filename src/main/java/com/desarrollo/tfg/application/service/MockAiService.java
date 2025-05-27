package com.desarrollo.tfg.application.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("mock")
public class MockAiService implements AiService {
  @Override
  public String ask(String prompt) {
    // Respuesta simulada concatenando
    return "[MOCK AI] He recibido tu mensaje: '" + prompt + "'";
  }
}
