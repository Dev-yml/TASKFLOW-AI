# TaskFlow AI

**An enterprise-grade AI-powered Task Manager, CRM Pipeline, and Team Collaboration platform built with React and Spring Boot.**

Live Repo → [github.com/Arjunsingh-7/TaskFlow-AI](https://github.com/Arjunsingh-7/TaskFlow-AI)

---

## What It Does

TaskFlow AI combines four tools teams usually pay for separately — into one workspace:

| Module | What it does |
|---|---|
| Task Management | Kanban board with drag-and-drop, status tracking, AI priority suggestions |
| CRM Pipeline | Lead management from LEAD → QUALIFIED → PROPOSAL → WON/LOST |
| Team Chat | Real-time messaging, file sharing (PDF/DOCX/images), block/remove/delete |
| AI Insights | Workspace health score, project risks, workload analysis, CRM alerts |

---

## Tech Stack

**Frontend**
- React 18 + Vite
- Redux Toolkit + TanStack Query
- Tailwind CSS + Framer Motion
- SockJS + STOMP (WebSocket)
- Recharts (analytics charts)

**Backend**
- Spring Boot 3 + Java 21
- Spring Security (JWT + OAuth2 — Google & GitHub)
- Spring Data JPA + Hibernate
- WebSocket (STOMP in-memory broker)
- Redis (presence tracking + caching)

**Database & Infrastructure**
- PostgreSQL 15
- Redis 7
- Docker + Docker Compose
- Groq API (llama-3.3-70b-versatile) for AI features

---

## Features

### Authentication
- Email/password login with JWT (24-hour token)
- Google OAuth2 and GitHub OAuth2 login
- Fully stateless backend — no sessions
- Auto token expiry detection on frontend
- Session cleanup on logout (no stale workspace state)

### Workspaces & Projects
- Create workspaces, invite members with roles (ADMIN / MEMBER)
- Projects belong to workspaces; tasks inherit workspace
- Project status: ACTIVE / COMPLETED / ARCHIVED

### Task Management (Kanban)
- Drag-and-drop board with `@dnd-kit/core`
- Task statuses: TODO → IN_PROGRESS → IN_REVIEW → DONE → CANCELLED
- Priority levels: LOW / MEDIUM / HIGH / URGENT
- Due date picker with LocalDate-compatible formatting
- AI priority suggestion and deadline prediction via Groq API
- Real-time updates via WebSocket — all users see moves instantly
- Task comments, activity log, file attachments

### CRM Pipeline
- Lead pipeline: LEAD → QUALIFIED → PROPOSAL → NEGOTIATION → WON / LOST
- Deal value tracking, assigned user, follow-up via `lastActivityAt`
- Lead analytics: conversion rates, pipeline value by stage
- Filter by status, priority, assignee, search

### Team Chat
- Private DMs and group chat rooms
- Real-time messages via STOMP WebSocket
- File sharing: PDF, DOCX, XLSX, PPTX, images (up to 20 MB)
- Online presence with Redis heartbeat keys
- Full presence snapshot on connect — all users see who's online immediately
- Block user, Remove from group, Delete chat
- Typing indicators

### AI Insights Dashboard
- Per-workspace health score (0–100) from real DB data
- Project risk detection, team workload analysis
- CRM lead inactivity alerts (7+ days no follow-up)
- AI recommendations with one-click actions
- Weekly summary, 7-day trend charts
- Daily snapshots in `ai_insight_snapshots` table

### Analytics
- Task completion rates, status and priority distribution
- Team performance rankings by activity score
- CRM deal value funnel, lead conversion rates
- Activity heatmap (last 35 days)

### Notifications
- Real-time delivery via user-specific WebSocket queue
- Persistent in DB — unread count badge in sidebar

### AI Copilot
- Floating chatbot on every page
- Page-aware context (knows which feature you're using)
- Groq-powered with conversation history

---

## Project Structure

```
TaskFlow-AI/
├── crm-backend/          # Spring Boot backend
│   ├── src/main/java/com/arjun/crm/
│   │   ├── ai/           # AI controllers, services, providers
│   │   ├── config/       # Security, WebSocket, CORS, MVC config
│   │   ├── controller/   # REST controllers
│   │   ├── entity/       # JPA entities
│   │   ├── enums/        # TaskStatus, LeadStatus, etc.
│   │   ├── repository/   # Spring Data JPA repos
│   │   ├── security/     # JWT filter, OAuth2 handler
│   │   └── service/      # Business logic implementations
│   ├── Dockerfile
│   ├── docker-compose.yml
│   └── .env.example
│
└── crm-frontend/         # React + Vite frontend
    ├── src/
    │   ├── components/   # Chat, Kanban, AI, CRM, common components
    │   ├── hooks/        # useAuth, useChat, useStreamingText
    │   ├── pages/        # Dashboard, Analytics, AIInsights, Chat, CRM
    │   ├── services/     # Axios service files (api, chat, task, ai, crm)
    │   └── store/        # Redux slices (auth, workspace, chat, presence)
    └── .env.example
```

---

## Getting Started

### Prerequisites
- Docker Desktop
- Node.js 18+
- A [Groq API key](https://console.groq.com) (free tier works)
- Google OAuth credentials (optional)
- GitHub OAuth credentials (optional)

### 1. Clone the repo

```bash
git clone https://github.com/Arjunsingh-7/TaskFlow-AI.git
cd TaskFlow-AI
```

### 2. Configure backend environment

```bash
cd crm-backend
cp .env.example .env
```

Edit `.env` and fill in at minimum:

```
JWT_SECRET=any_random_32_character_string_here
XAI_API_KEY=gsk_your_groq_api_key
DATABASE_PASSWORD=postgres
```

For Google and GitHub login, add:

```
GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_CLIENT_SECRET=your_google_client_secret
GITHUB_CLIENT_ID=your_github_client_id
GITHUB_CLIENT_SECRET=your_github_client_secret
```

### 3. Start backend

```bash
docker compose up -d --build
```

This starts PostgreSQL, Redis, and Spring Boot. All database tables are auto-created by Hibernate on first run.

### 4. Start frontend

```bash
cd ../crm-frontend
cp .env.example .env
npm install
npm run dev
```

Open **http://localhost:3000**

---

## Environment Variables

### Backend `.env`

| Variable | Required | Description |
|---|---|---|
| `JWT_SECRET` | ✅ | Min 32 chars, any random string |
| `XAI_API_KEY` | ✅ | Groq API key |
| `DATABASE_PASSWORD` | ✅ | PostgreSQL password |
| `GOOGLE_CLIENT_ID` | Optional | Google OAuth |
| `GOOGLE_CLIENT_SECRET` | Optional | Google OAuth |
| `GITHUB_CLIENT_ID` | Optional | GitHub OAuth |
| `GITHUB_CLIENT_SECRET` | Optional | GitHub OAuth |
| `UPLOAD_DIR` | Optional | Default: `/tmp/uploads` |

### Frontend `.env`

| Variable | Description |
|---|---|
| `VITE_API_URL` | Backend base URL (default: proxied via Vite) |
| `VITE_WS_URL` | WebSocket URL (default: `http://localhost:8080/ws`) |

---

## Key API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/login` | Email/password login |
| POST | `/api/auth/register` | Register new user |
| GET | `/api/workspaces` | List user workspaces |
| POST | `/api/tasks` | Create task |
| PATCH | `/api/tasks/{id}/status` | Update task status |
| GET | `/api/leads/workspace/{id}/analytics` | CRM analytics |
| GET | `/api/ai-insights/dashboard/{workspaceId}` | AI workspace insights |
| POST | `/api/chat/messages/upload?roomId={id}` | Upload file to chat |
| POST | `/api/chat/rooms/block/{userId}` | Block a user |
| DELETE | `/api/chat/rooms/{id}` | Delete a chat room |

---

## 5-Week Development Plan

### Week 1 — Core Architecture & Auth
- Project scaffolding: Spring Boot + React + Docker Compose
- PostgreSQL schema: users, workspaces, projects, tasks
- JWT authentication (register, login, token validation)
- Spring Security stateless filter chain
- Redux auth slice, Axios interceptor with token attachment
- Protected routes and basic dashboard shell

### Week 2 — Task Management & Kanban
- Task CRUD REST endpoints
- Kanban board with drag-and-drop (`@dnd-kit/core`)
- Task statuses, priorities, due date, assignee
- Task comments, activity timeline, file attachments
- WebSocket real-time task updates
- AI task prioritization and deadline prediction (Groq)

### Week 3 — CRM Pipeline & Chat System
- Lead entity with pipeline stages and deal value
- CRM board, filters, analytics
- Chat rooms (private + group)
- STOMP WebSocket messaging with SockJS
- Redis presence tracking with heartbeat keys
- Chat file upload (stored in `/tmp/uploads/chat/`)
- Block user, remove from group, delete chat

### Week 4 — AI Insights & Analytics
- Workspace health score engine
- Project risk detection, workload analysis
- CRM lead inactivity detection
- AI recommendations with actionable payloads
- 7-day trend snapshots in database
- Analytics dashboard with Recharts
- Groq integration for natural language insights

### Week 5 — OAuth2, Notifications & Polish
- Google and GitHub OAuth2 login
- Dual security chain (OAuth2 PKCE + Stateless API)
- Notification system (WebSocket + DB persistence)
- Global AI Copilot with page context
- Session expiry and stale state bug fixes
- Docker production hardening (upload dir permissions)
- `.env.example` and `.gitignore` for safe open-source publishing

---

## Architecture Decisions

**Stateless backend** — `STATELESS` session policy for all API routes. OAuth2 uses a separate `@Order(1)` filter chain with `IF_REQUIRED` only for the PKCE redirect handshake.

**Redis presence** — Per-user heartbeat key with 60s TTL, refreshed every 30s from the frontend. Prevents the shared-SET expiry bug. Full snapshot broadcast on each STOMP connect so all users see who's online immediately.

**LocalDate formatting** — Frontend explicitly converts JavaScript `Date` objects to `yyyy-MM-dd` strings before sending to the backend. Jackson cannot deserialize ISO datetime strings (`2026-06-15T18:30:00.000Z`) into `LocalDate`.

**Docker upload dir** — Files written to `/tmp/uploads/` created with `spring:spring` ownership in the Dockerfile before the `USER spring` directive. Named volumes overlay would reset ownership, so no volume is mounted — files live in the container.

---

## License

MIT

---

Made by [Arjun Singh](https://github.com/Arjunsingh-7)
