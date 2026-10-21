package com.example.spring_rag.dto;

public record CitationDto (
        String filePath,
        Integer startLine,
        Integer endLine,
        String language)
{ }
