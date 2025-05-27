package com.desarrollo.tfg.domain.repository;

import com.desarrollo.tfg.domain.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
  Optional<Conversation> findByIdAndUserId(Long id, Long userId);
}
