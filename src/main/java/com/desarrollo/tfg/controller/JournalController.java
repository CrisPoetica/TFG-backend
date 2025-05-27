package com.desarrollo.tfg.controller;

import com.desarrollo.tfg.application.dto.journal.JournalEntryRequest;
import com.desarrollo.tfg.application.dto.journal.JournalEntryResponse;
import com.desarrollo.tfg.application.service.JournalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/journal")
@RequiredArgsConstructor
public class JournalController {
  private final JournalService service;

  @PostMapping
  public ResponseEntity<JournalEntryResponse> create(@RequestBody JournalEntryRequest req) {
    return ResponseEntity.ok(service.create(req));
  }

  @GetMapping
  public ResponseEntity<List<JournalEntryResponse>> getAll() {
    return ResponseEntity.ok(service.getAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<JournalEntryResponse> getById(@PathVariable Long id) {
    return ResponseEntity.ok(service.getById(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<JournalEntryResponse> update(
    @PathVariable Long id,
    @RequestBody JournalEntryRequest req
  ) {
    return ResponseEntity.ok(service.update(id, req));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
