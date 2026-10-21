package com.example.spring_rag.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.spring_rag.entity.ChatSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatSessionRepository extends JpaRepository<ChatSessionEntity, UUID> {
    List<ChatSessionEntity> findByUserIdAndRepositoryIdOrderByCreatedAtDesc(UUID userId, UUID repositoryId);

    Optional<ChatSessionEntity> findByIdAndUserId(UUID id, UUID userId);
}
