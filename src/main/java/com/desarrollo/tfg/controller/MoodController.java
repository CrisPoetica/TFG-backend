package com.desarrollo.tfg.controller;

import com.desarrollo.tfg.application.dto.mood.MoodEntryRequest;
import com.desarrollo.tfg.application.dto.mood.MoodEntryResponse;
import com.desarrollo.tfg.application.service.MoodService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users/{userId}/moods")
public class MoodController {

  private final MoodService service;

  public MoodController(MoodService service) {
    this.service = service;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public MoodEntryResponse create(
    @PathVariable Long userId,
    @RequestBody MoodEntryRequest req
  ) {
    return service.create(userId, req);
  }

  @GetMapping
  public List<MoodEntryResponse> listByRange(
    @PathVariable Long userId,
    @RequestParam String startDate,
    @RequestParam String endDate
  ) {
    return service.findByRange(userId, startDate, endDate);
  }

  @GetMapping("/date/{date}")
  public MoodEntryResponse getByDate(
    @PathVariable Long userId,
    @PathVariable String date
  ) {
    return service.findByDate(userId, date);
  }

  @PutMapping("/{id}")
  public MoodEntryResponse update(
    @PathVariable Long userId,
    @PathVariable Long id,
    @RequestBody MoodEntryRequest req
  ) {
    return service.update(userId, id, req);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(
    @PathVariable Long userId,
    @PathVariable Long id
  ) {
    service.delete(userId, id);
  }
}
