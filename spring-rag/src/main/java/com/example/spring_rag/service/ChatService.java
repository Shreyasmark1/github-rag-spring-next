package com.example.spring_rag.service;

import java.util.List;
import java.util.UUID;

import com.example.spring_rag.dto.ChatMessageRes;
import com.example.spring_rag.dto.ChatSessionReq;
import com.example.spring_rag.dto.ChatSessionRes;
import com.example.spring_rag.entity.ChatMessageEntity;
import com.example.spring_rag.entity.ChatSessionEntity;
import com.example.spring_rag.entity.RepositoryEntity;
import com.example.spring_rag.entity.enums.IndexStatus;
import com.example.spring_rag.entity.enums.MessageRole;
import com.example.spring_rag.exceptions.ApiException;
import com.example.spring_rag.repository.ChatMessageRepository;
import com.example.spring_rag.repository.ChatSessionRepository;
import com.example.spring_rag.service.ai.ChatPromptBuilder;
import com.example.spring_rag.service.ai.ChatStreamHandler;
import com.example.spring_rag.service.ai.CitationMapper;
import com.example.spring_rag.service.ai.CodeContextRetriever;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.RequiredArgsConstructor;

/**
 * Chat sessions and the RAG chat pipeline entry point.
 *
 * <p>{@link #streamReply} orchestrates the full flow:
 * validate → save user message → retrieve code context → build prompts → stream AI reply.
 * Each step is implemented in a dedicated class under {@code service.ai}.
 */
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final RepoService repoService;
    private final CodeContextRetriever codeContextRetriever;
    private final ChatPromptBuilder chatPromptBuilder;
    private final ChatStreamHandler chatStreamHandler;
    private final CitationMapper citationMapper;

    @Transactional
    public ChatSessionRes createSession(UUID userId, ChatSessionReq request) {

        RepositoryEntity repo = repoService.requireOwned(request.repositoryId(), userId);

        if (repo.getIndexStatus() != IndexStatus.READY) {
            throw ApiException.badRequest("Repository must be indexed before chatting");
        }

        String title = request.title() != null && !request.title().isBlank()
                ? request.title()
                : "Chat with " + repo.getFullName();

        ChatSessionEntity session = ChatSessionEntity.builder()
                .userId(userId)
                .repositoryId(repo.getId())
                .title(title)
                .build();
        session = chatSessionRepository.save(session);
        return ChatSessionRes.fromEntity(session);
    }

    @Transactional(readOnly = true)
    public List<ChatSessionRes> listSessions(UUID userId, UUID repositoryId) {
        repoService.requireOwned(repositoryId, userId);
        return chatSessionRepository
                .findByUserIdAndRepositoryIdOrderByCreatedAtDesc(userId, repositoryId)
                .stream()
                .map(ChatSessionRes::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ChatMessageRes> getMessages(UUID userId, UUID sessionId) {
        ChatSessionEntity session = requireSession(userId, sessionId);
        return chatMessageRepository.findBySessionIdOrderByCreatedAtAsc(session.getId()).stream()
                .map(this::toMessageResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ChatSessionEntity requireSession(UUID userId, UUID sessionId) {
        return chatSessionRepository.findByIdAndUserId(sessionId, userId)
                .orElseThrow(() -> ApiException.notFound("Chat session not found"));
    }

    public SseEmitter streamReply(UUID userId, UUID sessionId, String userContent) {
        // 1. Ensure the session exists and the repo is indexed
        ChatSessionEntity session = requireSession(userId, sessionId);
        RepositoryEntity repo = repoService.requireOwned(session.getRepositoryId(), userId);
        if (repo.getIndexStatus() != IndexStatus.READY) {
            throw ApiException.badRequest("Repository is not ready for chat");
        }

        // 2. Persist the user's message
        ChatMessageEntity userMessage = chatMessageRepository.save(ChatMessageEntity.builder()
                .sessionId(session.getId())
                .role(MessageRole.USER)
                .content(userContent)
                .build());

        // 3. RAG retrieval — find code chunks similar to the question
        var retrievedContext = codeContextRetriever.retrieve(repo.getId(), userContent);

        // 4. Build LLM prompts from retrieved context + question
        String systemPrompt = chatPromptBuilder.systemPrompt(repo.getFullName());
        String userPrompt = chatPromptBuilder.userPrompt(retrievedContext.contextText(), userContent);

        // 5. Stream OpenAI response to the client (SSE)
        return chatStreamHandler.stream(
                session.getId(),
                toMessageResponse(userMessage),
                retrievedContext.citations(),
                systemPrompt,
                userPrompt
        );
    }

    private ChatMessageRes toMessageResponse(ChatMessageEntity message) {
        return new ChatMessageRes(
                message.getId(),
                message.getRole(),
                message.getContent(),
                citationMapper.fromJson(message.getCitations()),
                message.getCreatedAt());
    }
}
