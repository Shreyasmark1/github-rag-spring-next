package com.example.spring_rag.dto;

import com.example.spring_rag.entity.UserEntity;

import java.util.UUID;

public record UserResponse(
    UUID id,
    Long githubId,
    String githubUsername,
    String displayName,
    String avatarUrl
) {

    public static UserResponse fromEntity(UserEntity user){
        return new UserResponse(
                user.getId(),
                user.getGithubId(),
                user.getGithubUsername(),
                user.getDisplayName(),
                user.getAvatarUrl()
        );
    }
}
