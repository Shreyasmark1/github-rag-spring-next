# Architecture

## System Overview

Tech stack versions live in the [README](../README.md#tech-stack). In short: Next.js frontend, Spring Boot API, PostgreSQL + pgvector storage, Spring AI with Google GenAI for embeddings and chat, GitHub API for repo data.

## High-Level Diagram

```text
Browser (Next.js App)
  │
  ├─ REST API calls (fetch with credentials)
  │   └─ Spring Boot API (/api/*)
  │      ├─ Auth (OAuth2 GitHub)
  │      ├─ Repo Service (sync from GitHub API)
  │      ├─ Indexing Service (async, chunks + embeddings → pgvector)
  │      └─ Chat Service (RAG retrieval + LLM streaming via SSE)
  │
  ├─ OAuth redirect flow → GitHub
  │
  ├─ SSE stream (/api/chat/sessions/{id}/messages) for token streaming
  │
  └─ Static assets (Next.js)

External:
- GitHub API (list repos, get tree, file contents)
- Google GenAI (embeddings, chat model) via Spring AI

Data:
- PostgreSQL: users, repositories, chat_sessions, chat_messages
- pgvector: embeddings with metadata (repoId, filePath, language, chunkIndex)
```

## Backend Components

| Layer | Key Classes | Purpose |
|---|---|---|
| Controller | AuthController, RepoController, ChatController | HTTP endpoints, request validation |
| Service | UserService, RepoService, ChatService | Business logic |
| Indexing | IndexingService, CodeChunker, CodeFileFilter, GitHubApiClient, GitHubRateLimiter | Repo ingestion, chunking, embedding, storage |
| AI/RAG | CodeContextRetriever, ChatPromptBuilder, ChatStreamHandler, CitationMapper, RagSettings | Retrieval, prompting, streaming, citations |
| Security | SecurityConfig, GithubOAuth2UserService, UserSession, AppUserPrinciple, CryptoConfig | OAuth2, session, token encryption |
| Config | AppConfig (async), CorsConfig, CryptoConfig | Cross-cutting config |

## Frontend Structure

| Area | Purpose |
|---|---| 
| app/ | App Router pages (auth, dashboard, chat/[repoId]) |
| components/ | UI + feature components (chat, dashboard, layout, ui primitives) |
| hooks/ | TanStack Query hooks (use-auth, use-repos, use-chat) |
| lib/ | API client, SSE streaming, query keys, utils |

## Data Model (Core Entities)

- UserEntity: GitHub profile, encrypted access token, scopes
- RepositoryEntity: GitHub repo metadata, index status, progress counters
- ChatSessionEntity: per-repo chat sessions
- ChatMessageEntity: USER/ASSISTANT messages with content and citations JSON

## RAG Pipeline

1. Fetch repo tree (recursive) from GitHub API (authenticated)
2. Filter blobs by file extension, size (max-file-bytes), skip binaries/ignored patterns
3. Chunk files: prepend file header, split by tokens (TokenTextSplitter) → chunks with metadata
4. Embed chunks via Spring AI vector store (Google GenAI) and store in pgvector with filters
5. Query: embed user question, similarity search with filter `repoId == repoId`, topK=8
6. Prompt: system prompt + code context + user question (no full history in current impl)
7. Stream: LLM tokens via SSE to client; persist assistant message with citations

## Security Notes

- GitHub OAuth2 with session-based auth (cookies)
- Access tokens encrypted at rest (AES/CBC with hex encoding)
- CORS configured with allowed origins + credentials
- CSRF disabled (stateless API style with sessions); uses SameSite cookies
