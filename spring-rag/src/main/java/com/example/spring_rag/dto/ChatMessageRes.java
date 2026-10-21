package com.example.spring_rag.dto;

import com.example.spring_rag.entity.ChatMessageEntity;
import com.example.spring_rag.entity.enums.MessageRole;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChatMessageRes(
        UUID id,
        MessageRole role,
        String content,
        List<CitationDto> citations,
        Instant createdAt
) {
    public static ChatMessageRes fromEntity(ChatMessageEntity message, List<CitationDto> citations){
        return new ChatMessageRes(
                message.getId(),
                message.getRole(),
                message.getContent(),
                citations,
                message.getCreatedAt()
        );
    }
}
