package com.example.spring_rag.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "chat_sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatSessionEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "repository_id", nullable = false)
    private UUID repositoryId;

    @Column(nullable = false, length = 200)
    private String title;

    @PrePersist
    protected void ensureDefaults() {
        if (title == null || title.isBlank()) {
            title = "New chat";
        }
    }

}
