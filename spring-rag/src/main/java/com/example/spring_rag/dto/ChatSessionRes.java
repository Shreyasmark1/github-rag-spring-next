package com.example.spring_rag.dto;

import com.example.spring_rag.entity.ChatSessionEntity;

import java.time.Instant;
import java.util.UUID;

public record ChatSessionRes(
        UUID id,
        UUID repositoryId,
        String title,
        Instant createdAt
) {
    public static ChatSessionRes fromEntity(ChatSessionEntity session){
        return new ChatSessionRes(
                session.getId(),
                session.getRepositoryId(),
                session.getTitle(),
                session.getCreatedAt()
        );
    }
}
