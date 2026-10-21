package com.example.spring_rag.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.spring_rag.entity.RepositoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryRepo extends JpaRepository<RepositoryEntity, UUID> {
    List<RepositoryEntity> findByUserIdOrderByFullNameAsc(UUID userId);

    Optional<RepositoryEntity> findByIdAndUserId(UUID id, UUID userId);

    Optional<RepositoryEntity> findByUserIdAndGithubRepoId(UUID userId, Long githubRepoId);
}
