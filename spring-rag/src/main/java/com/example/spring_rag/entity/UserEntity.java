package com.example.spring_rag.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@Builder
public class UserEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "github_id", unique = true, nullable = false)
    private Long githubId;
    @Column(name = "github_username", nullable = false)
    private String githubUsername;
    @Column(name = "display_name", nullable = false)
    private String displayName;
    @Column(name = "avatar_url")
    private String avatarUrl;
    @Column(name = "access_token")
    private String accessToken;
    @Column(name = "token_scopes")
    private String tokenScopes;


}
