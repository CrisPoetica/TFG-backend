package com.desarrollo.tfg.application.service;

import com.desarrollo.tfg.application.dto.journal.JournalEntryRequest;
import com.desarrollo.tfg.application.dto.journal.JournalEntryResponse;
import com.desarrollo.tfg.domain.entity.JournalEntry;
import com.desarrollo.tfg.domain.repository.JournalEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JournalService {
  private final JournalEntryRepository repo;
  private final UserService userService;

  @Transactional
  public JournalEntryResponse create(JournalEntryRequest req) {
    Long userId = userService.getCurrentUser().getId();
    JournalEntry e = JournalEntry.builder()
      .userId(userId)
      .entryDate(req.getEntryDate())
      .content(req.getContent())
      .createdAt(LocalDateTime.now())
      .build();
    e = repo.save(e);
    return toDto(e);
  }

  public List<JournalEntryResponse> getAll() {
    Long userId = userService.getCurrentUser().getId();
    return repo.findAllByUserIdOrderByEntryDateDesc(userId)
      .stream().map(this::toDto)
      .collect(Collectors.toList());
  }

  public JournalEntryResponse getById(Long id) {
    Long userId = userService.getCurrentUser().getId();
    JournalEntry e = repo.findByIdAndUserId(id, userId)
      .orElseThrow(() -> new RuntimeException("Entrada no encontrada"));
    return toDto(e);
  }

  @Transactional
  public JournalEntryResponse update(Long id, JournalEntryRequest req) {
    Long userId = userService.getCurrentUser().getId();
    JournalEntry e = repo.findByIdAndUserId(id, userId)
      .orElseThrow(() -> new RuntimeException("Entrada no encontrada"));
    e.setEntryDate(req.getEntryDate());
    e.setContent(req.getContent());
    e = repo.save(e);
    return toDto(e);
  }

  @Transactional
  public void delete(Long id) {
    Long userId = userService.getCurrentUser().getId();
    JournalEntry e = repo.findByIdAndUserId(id, userId)
      .orElseThrow(() -> new RuntimeException("Entrada no encontrada"));
    repo.delete(e);
  }

  private JournalEntryResponse toDto(JournalEntry e) {
    JournalEntryResponse r = new JournalEntryResponse();
    r.setId(e.getId());
    r.setEntryDate(e.getEntryDate());
    r.setContent(e.getContent());
    r.setCreatedAt(e.getCreatedAt());
    return r;
  }
}
