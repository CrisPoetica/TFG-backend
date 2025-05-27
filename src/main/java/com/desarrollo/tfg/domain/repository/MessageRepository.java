package com.desarrollo.tfg.domain.repository;

import com.desarrollo.tfg.domain.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
  List<Message> findAllByConversationIdOrderBySentAtAsc(Long conversationId);
}
