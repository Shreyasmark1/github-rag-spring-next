package com.example.spring_rag.dto;

import com.example.spring_rag.entity.enums.IndexStatus;

import java.time.Instant;
import java.util.UUID;

public record IndexStatusRes(
        UUID repositoryId,
        IndexStatus indexStatus,
        int filesTotal,
        int filesProcessed,
        int chunkCount,
        Instant indexedAt,
        String errorMessage
) {
}
