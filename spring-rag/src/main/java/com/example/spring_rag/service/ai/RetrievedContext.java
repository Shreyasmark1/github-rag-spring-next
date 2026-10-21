package com.example.spring_rag.service.ai;

import com.example.spring_rag.dto.CitationDto;

import java.util.List;

public record RetrievedContext(
        List<CitationDto> citations,
        String contextText
) {
}
