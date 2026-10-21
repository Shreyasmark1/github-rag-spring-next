package com.example.spring_rag.dto;

import com.example.spring_rag.entity.RepositoryEntity;
import com.example.spring_rag.entity.enums.IndexStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.UUID;

public record RepositoryRes(
        UUID id,
        Long githubRepoId,
        String owner,
        String name,
        String fullName,
        @JsonProperty("isPrivate")
        boolean isPrivate,
        String defaultBranch,
        String language,
        String htmlUrl,
        String description,
        IndexStatus indexStatus,
        Instant indexedAt,
        int chunkCount,
        int filesTotal,
        int filesProcessed,
        String errorMessage
) {
        public static RepositoryRes fromEntity(RepositoryEntity entity) {
                return new RepositoryRes(
                                entity.getId(),
                                entity.getGithubRepoId(),
                                entity.getOwner(),
                                entity.getName(),
                                entity.getFullName(),
                                entity.isPrivate(),
                                entity.getDefaultBranch(),
                                entity.getLanguage(),
                                entity.getHtmlUrl(),
                                entity.getDescription(),
                                entity.getIndexStatus(),
                                entity.getIndexedAt(),
                                entity.getChunkCount(),
                                entity.getFilesTotal(),
                                entity.getFilesProcessed(),
                                entity.getErrorMessage()
                );
        }
}
