package com.example.spring_rag.dto;

import jakarta.validation.constraints.NotBlank;

public record ChatMessageReq(@NotBlank String content) {
}
