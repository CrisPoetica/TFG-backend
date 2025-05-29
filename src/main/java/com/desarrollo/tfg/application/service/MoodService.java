package com.desarrollo.tfg.application.service;

import com.desarrollo.tfg.domain.entity.MoodEntry;
import com.desarrollo.tfg.domain.repository.MoodEntryRepository;
import com.desarrollo.tfg.application.dto.mood.MoodEntryRequest;
import com.desarrollo.tfg.application.dto.mood.MoodEntryResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MoodService {

  private final MoodEntryRepository repo;

  public MoodService(MoodEntryRepository repo) {
    this.repo = repo;
  }

  public MoodEntryResponse create(Long userId, MoodEntryRequest req) {
    MoodEntry e = MoodEntry.builder()
      .userId(userId)
      .entryDate(LocalDate.parse(req.getDate()))
      .mood(req.getMood())
      .notes(req.getNotes())
      .build();
    MoodEntry saved = repo.save(e);
    return toDto(saved);
  }

  public List<MoodEntryResponse> findByRange(Long userId, String start, String end) {
    return repo
      .findAllByUserIdAndEntryDateBetweenOrderByEntryDate(
        userId, LocalDate.parse(start), LocalDate.parse(end)
      )
      .stream()
      .map(this::toDto)
      .collect(Collectors.toList());
  }

  public MoodEntryResponse findByDate(Long userId, String date) {
    MoodEntry e = repo.findByUserIdAndEntryDate(userId, LocalDate.parse(date))
      .orElseThrow(() -> new RuntimeException("No mood for " + date));
    return toDto(e);
  }

  public MoodEntryResponse update(Long userId, Long id, MoodEntryRequest req) {
    MoodEntry e = repo.findById(id)
      .orElseThrow(() -> new RuntimeException("Mood entry not found: " + id));
    if (!e.getUserId().equals(userId)) {
      throw new RuntimeException("Cannot edit another user’s entry");
    }
    e.setMood(req.getMood());
    e.setNotes(req.getNotes());
    // entryDate no lo tocamos: si quieres permitirlo haz e.setEntryDate(...)
    MoodEntry saved = repo.save(e);
    return toDto(saved);
  }

  public void delete(Long userId, Long id) {
    MoodEntry e = repo.findById(id)
      .orElseThrow(() -> new RuntimeException("Not found: " + id));
    if (!e.getUserId().equals(userId)) {
      throw new RuntimeException("Cannot delete another user’s entry");
    }
    repo.delete(e);
  }

  private MoodEntryResponse toDto(MoodEntry e) {
    return new MoodEntryResponse(
      e.getId(),
      e.getUserId(),
      e.getEntryDate().toString(),
      e.getMood(),
      e.getNotes(),
      e.getCreatedAt().toString(),
      e.getUpdatedAt().toString()
    );
  }
}
