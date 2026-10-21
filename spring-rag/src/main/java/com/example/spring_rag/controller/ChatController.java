package com.example.spring_rag.controller;

import com.example.spring_rag.dto.*;
import com.example.spring_rag.security.UserSession;
import com.example.spring_rag.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final UserSession userSession;
    private final ChatService chatService;

    @PostMapping("/sessions")
    public ApiResponse<ChatSessionRes> createSession(@Valid @RequestBody ChatSessionReq body) {
        ChatSessionRes responseBody = chatService.createSession(userSession.getId(), body);
        return ApiResponse.success(responseBody);
    }

    @GetMapping("/sessions")
    public ApiResponse<List<ChatSessionRes>> listSessions(@RequestParam UUID repositoryId) {
        List<ChatSessionRes> responseBody = chatService.listSessions(userSession.getId(), repositoryId);
        return ApiResponse.success(responseBody);
    }

    @GetMapping("/sessions/{id}")
    public ApiResponse<List<ChatMessageRes>> getMessages(@PathVariable UUID id) {
        UUID userId = userSession.getId();
        List<ChatMessageRes> responseBody = chatService.getMessages(userId, id);
        return ApiResponse.success(responseBody);
    }

    @PostMapping(value = "/sessions/{id}/messages", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter sendMessage(
            @PathVariable UUID id,
            @Valid @RequestBody ChatMessageReq request
    ) {
        UUID userId = userSession.getId();
        return chatService.streamReply(userId, id, request.content());
    }
}
