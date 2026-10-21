# Setup Instructions

This document covers local setup for both frontend (Next.js) and backend (Spring Boot).

## Prerequisites

- Java 25 (or compatible; project uses Java toolchain 25)
- Node.js 18+ (Next.js 16 requires modern Node)
- PostgreSQL 14+ with pgvector extension enabled
- GitHub OAuth App credentials
- Google AI API key (for embeddings/chat via Spring AI Google GenAI)

## PostgreSQL Setup

1. Install PostgreSQL and enable pgvector:
```sql
CREATE EXTENSION IF NOT EXISTS vector;
CREATE DATABASE repo_ai;
```
2. Note host/port/user/password for connection.

## Backend (spring-rag)

1. Configure environment variables or edit `src/main/resources/application.properties`. Recommended: use environment variables for secrets.
   Key properties:
   - `spring.datasource.url`, `spring.datasource.username`, `spring.datasource.password`
   - `spring.security.oauth2.client.registration.github.client-id`, `client-secret`
   - `app.frontend-url` (e.g. http://localhost:3000)
   - `app.cors.allowed-origins` (e.g. http://localhost:3000)
   - `app.token-encryptor-password`, `app.token-encryptor-salt` (strong random values)
   - `spring.ai.google.genai.api-key` (or via env)

2. Build and run:
```bash
cd spring-rag
./gradlew bootRun
```
Backend runs on default port (check logs). App base path is `/app` per config (`server.contextPath=/app`).

3. Verify health (if actuator enabled) or hit auth endpoints as needed.

## Frontend (next-ui)

1. Create `.env` from `.env.example` or set variables:
```bash
NEXT_PUBLIC_API_BASE_URL=http://localhost:8080
```
Note: backend context path is `/app` in application.properties, but frontend API calls use paths like `/api/...` and construct full URL from base. Ensure CORS/origins align.

2. Install deps and run:
```bash
cd next-ui
npm install
npm run dev
```
Open http://localhost:3000.

## OAuth Flow Notes

- Register GitHub OAuth App with callback: `http://localhost:8080/login/oauth2/code/github` (Spring Security default)
- Set client-id/secret in backend config.

## Troubleshooting

- CORS issues: ensure `app.cors.allowed-origins` includes frontend origin and `credentials: include` is used (frontend does).
- SSE may be buffered by proxies in prod; disable buffering.
- Indexing large repos: may take time; status polling updates UI.
