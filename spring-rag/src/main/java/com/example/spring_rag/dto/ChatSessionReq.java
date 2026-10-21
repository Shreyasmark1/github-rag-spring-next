package com.example.spring_rag.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ChatSessionReq(
        @NotNull UUID repositoryId,
        String title
) {
}
