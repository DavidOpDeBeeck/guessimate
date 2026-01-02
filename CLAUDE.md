# AGENTS.md

This file provides guidance to AI coding assistants when working with code in this repository.

## General Explanation

**Guessimate** is a real-time collaborative estimation application (planning poker) for distributed agile teams. Team members simultaneously select estimates from a shared deck, with real-time synchronization via WebSockets.

**Core Purpose**: Enable distributed teams to estimate work items collaboratively with features like auto-reveal, emoji reactions, configurable timers, and role-based controls (Estimator/Observer).

**Technical Stack**:
- **Backend**: Java 25, Spring Boot 3.4.12, PostgreSQL, WebSocket, SMD Framework (CQRS)
- **Frontend**: Angular 20, TypeScript 5.8, TailwindCSS 4, Signals + RxJS
- **Build**: Gradle multi-module monorepo with Kotlin DSL
- **Observability**: OpenTelemetry, Micrometer, Prometheus

**Key Architectural Patterns**:
- **Hexagonal Architecture** (Ports & Adapters) - strict layer boundaries
- **CQRS** - Command Query Responsibility Segregation via SMD framework
- **Event Sourcing** - aggregates emit events, state built from events
- **Event-Driven** - modules communicate via domain events

---

## Module Overview

```
guessimate/
├── guessimate-api/              # Spring Boot application entry point
├── guessimate-lobby/            # Real-time collaboration
│   ├── api/                     # Public contracts (Commands, Queries, Events)
│   └── application/             # Implementation (domain, handlers, adapters)
├── guessimate-session/          # Persistent session & history
│   ├── api/                     # Public contracts
│   └── application/             # Implementation
└── guessimate-ui/               # Angular frontend
```

### guessimate-api
**Purpose**: Spring Boot application entry point that combines all modules.

**Key Responsibilities**:
- Application bootstrap and configuration
- Observability (OpenTelemetry tracing, Prometheus metrics)
- Command/Query bus interceptors for metrics and tracing
- Database configuration and migrations

**Key Files**:
- `GuessimateApplication.java` - main class importing Session and Lobby configurations
- `CommandBusMetricsInterceptor.java` - command execution metrics
- `QueryBusMetricsInterceptor.java` - query performance metrics

### guessimate-lobby
**Purpose**: Real-time, transient, in-memory collaborative estimation workspace.

**Key Responsibilities**:
- Manage connected users per session (WebSocket connections)
- Track current estimates during voting rounds
- Handle timers, auto-reveal, reactions
- Broadcast real-time updates to all participants

**Domain Model**:
- `Lobby` - aggregate root (event-sourced, ephemeral)
- `LobbyState` - mutable state container
- `User` - participant with role (ESTIMATOR/OBSERVER)

**Key Commands**: `ConnectUserToLobbyCommand`, `SetEstimateCommand`, `StartEstimationCommand`, `CompleteEstimationCommand`, `SetReactionCommand`, `SetAutoRevealCommand`

**Key Events**: `UserConnectedEvent`, `EstimateSetEvent`, `EstimationCompletedEvent`, `ReactionSetEvent`

**Key Queries**: `FindLobbyInfoForUserQuery`, `FindActiveLobbiesQuery`, `FindLobbyMetricsQuery`

### guessimate-session
**Purpose**: Persistent session management and estimation history (database-backed).

**Key Responsibilities**:
- Create sessions with human-readable slugs (e.g., "happy-tiger-mountain")
- Store completed estimations with insights
- Manage deck configurations
- Query historical data

**Domain Model**:
- `Session` - aggregate root (max 10 estimations)
- `Estimation` - completed round with votes and insights
- `Deck` - card value template (Fibonacci, T-shirt sizes, etc.)

**Insight Resolvers** (8 types):
- `ConsensusEstimationInsightResolver` - unanimous agreement
- `SoloEstimationInsightResolver` - single voter
- `OutlierPresentEstimationInsightResolver` - deviation detection
- And 5 more for various voting patterns

**Key Commands**: `CreateSessionCommand`, `AddEstimationCommand`

**Key Events**: `SessionCreatedEvent`, `EstimationAddedEvent`

**Key Queries**: `FindSessionByIdQuery`, `FindEstimationsBySessionIdQuery`, `FindDeckByNameQuery`

### guessimate-ui
**Purpose**: Angular 20 frontend with real-time WebSocket integration.

**Key Features**:
- `home` - session creation
- `session` - main estimation workspace
- `estimation` - voting controls and display
- `settings` - user preferences
- `history` - past estimations

**State Management**: Signals + Services (SessionStore), WebSocket for real-time

**Key Files**:
- `session.store.ts` - main state management
- `app.ts` - root component with routing

---

## Module Architecture

Each module follows **Hexagonal Architecture** with these layers:

```
module/
├── api/                         # PUBLIC CONTRACTS
│   ├── command/                 # Command definitions
│   ├── event/                   # Event definitions
│   └── query/                   # Query definitions + DTOs
└── application/                 # IMPLEMENTATION
    ├── domain/                  # Aggregate roots, value objects
    ├── usecase/                 # Command/Query handlers
    ├── port/                    # Interfaces for external dependencies
    ├── drivingadapter/          # REST controllers, WebSocket endpoints
    │   └── ws/                  # WebSocket handlers
    └── drivenadapter/           # Repository implementations, external services
        └── *view/               # Read models (CQRS projections)
```

**Layer Dependency Rules** (enforced by ArchUnit):
```
Api → Domain, Driving Adapter, Driven Adapter, Port, UseCase
Domain → Driven Adapter, Port, UseCase
Driving Adapter → Application, Driven Adapter
Driven Adapter → Application
Port → Driven Adapter, UseCase, Domain
UseCase → Application
```

**NEVER** let domain depend on infrastructure frameworks.

---

## Command, Event, and Query Handling

The application uses **SMD (Spring Mini Design)** framework for CQRS (`app.dodb:smd-spring-boot-starter`).

### How It Works

**Commands** - Write operations that change state:
```java
// Sent via CommandGateway
commandGateway.send(new CreateSessionCommand());

// Handlers marked with @CommandHandler
@CommandHandler
public void handle(ConnectUserToLobbyCommand command) { ... }
```

**Queries** - Read operations that return data:
```java
// Sent via QueryGateway
queryGateway.send(new FindSessionByIdQuery(sessionId));

// Handlers return Optional<T>, Set<T>, or direct T
@QueryHandler
public Optional<SessionTO> handle(FindSessionByIdQuery query) { ... }
```

**Events** - Notifications of state changes:
```java
// Published via EventPublisher
eventPublisher.publish(new UserConnectedEvent(...));

// Handlers react to events
@EventHandler
public void on(UserConnectedEvent event) { ... }
```

### Registering a New Command Handler

**Step 1**: Create command in `api/command/`:
```java
public record MyNewCommand(SessionId sessionId, String data) {}
```

**Step 2**: Create handler in `application/usecase/`:
```java
@Component
public class MyNewCommandHandler {

    @CommandHandler
    public void handle(MyNewCommand command) {
        // Implementation
    }
}
```

**Step 3**: Ensure configuration has `@EnableSMD`:
```java
@Configuration
@ComponentScan
@EnableSMD
public class MyModuleConfiguration {}
```

That's it! SMD auto-discovers handlers via component scanning.

### Registering a New Query Handler

**Step 1**: Create query in `api/query/`:
```java
public record FindMyDataQuery(SessionId sessionId) {}
```

**Step 2**: Create handler in `application/usecase/`:
```java
@Component
public class MyQueryHandler {

    @QueryHandler
    public Optional<MyDataTO> handle(FindMyDataQuery query) {
        return repository.findById(query.sessionId());
    }
}
```

### Registering a New Event Handler

**Step 1**: Create event in `api/event/`:
```java
public record MyNewEvent(SessionId sessionId, String data) implements LobbyEvent {}
```

**Step 2**: Add to sealed interface `permits` clause:
```java
public sealed interface LobbyEvent permits
    UserConnectedEvent,
    MyNewEvent,  // ADD HERE
    ... {}
```

**Step 3**: Create handler in `application/drivenadapter/`:
```java
@Component
@ProcessingGroup("my_processing_group")
public class MyEventHandler {

    @EventHandler
    public void on(MyNewEvent event) {
        // React to event
    }
}
```

**Step 4**: Configure processing group in configuration class:
```java
@Configuration
@EnableSMD
public class MyConfiguration {

    @Bean
    public ProcessingGroupsConfigurer myProcessingGroup() {
        return spec -> spec
            .processingGroup("my_processing_group").sync();
    }
}
```

**Processing Group Options**:
- `.sync()` - synchronous, same thread
- `.async().await()` - async, waits for completion
- `.async().fireAndForget()` - async, no waiting

### Command Handler Pattern (with Aggregate)

Standard pattern used in lobby module:

```java
@Component
public class LobbyCommandHandler {
    private final LobbyRepository lobbyRepository;
    private final EventPublisher eventPublisher;

    @CommandHandler
    public void handle(SetEstimateCommand command) {
        doWithLobby(command.sessionId(), lobby ->
            lobby.setEstimate(command.userId(), command.estimate())
        );
    }

    private void doWithLobby(SessionId sessionId, Consumer<Lobby> action) {
        var lobby = lobbyRepository.findBySessionId(sessionId);
        action.accept(lobby);
        lobby.consumeEvents().forEach(eventPublisher::publish);
    }
}
```

---

## Test Strategy

### Test Levels Overview

| Test Type | Location | Purpose | Speed | Dependencies |
|-----------|----------|---------|-------|--------------|
| Unit | `src/test/java` | Isolated logic | Fast | None |
| UseCase | `src/useCaseTest/java` | Handlers with stubs | Fast | SMDTestExtension |
| Integration | `src/integrationTest/java` | Full context + DB | Medium | TestContainers |
| Acceptance | `src/acceptanceTest/java` | E2E scenarios | Slow | Full app + HTTP |
| Architecture | `src/test/java` (api) | Layer boundaries | Fast | ArchUnit |

### What to Test Where

**Unit Tests** (`src/test/java`):
- Insight resolvers
- Utility classes
- Pure domain logic
- Value object validation

Example: `ConsensusEstimationInsightResolverTest.java`

**UseCase Tests** (`src/useCaseTest/java`):
- Command handlers
- Query handlers
- Event handlers
- **Uses stubs for repositories**

Pattern: `given(events) → when(command) → then(assertEvents)`

```java
@UseCaseTest
class ConnectUserToLobbyCommandHandlerTest extends CommandHandlerTestBase {

    @Test
    void connectsUserWithUsername() {
        given();  // Empty lobby

        smd.send(new ConnectUserToLobbyCommand(SESSION_ID, USER_ID, Optional.of("Alice")));

        assertThat(smd.getEvents())
            .containsExactly(new UserConnectedEvent(SESSION_ID, USER_ID, "Alice"));
    }
}
```

Base class provides: `given()`, `when()`, `smd.getEvents()`, stubs

**Integration Tests** (`src/integrationTest/java`):
- Repository implementations with real database
- Query handlers with JPA
- Read model projections

```java
@IntegrationTest
class SessionQueryIntegrationTest {
    @Inject QueryBus queryBus;
    @Inject EntityManager entityManager;

    @Test
    void findSessionById() {
        entityManager.persist(new SessionView(SESSION_ID, data));

        var result = queryBus.send(new FindSessionByIdQuery(SESSION_ID));

        assertThat(result).contains(expectedSession);
    }
}
```

**Acceptance Tests** (`src/acceptanceTest/java`):
- Full user workflows
- REST API endpoints
- End-to-end scenarios

```java
@AcceptanceTest
class SessionAcceptanceTest {
    // Full Spring context with random port
    // Tests HTTP endpoints
}
```

**Architecture Tests** (`guessimate-api/src/test/java`):
- Validates hexagonal layer boundaries
- Ensures no illegal dependencies

### Test Annotations

- `@UseCaseTest` - UseCase test with SMD infrastructure
- `@IntegrationTest` - Integration test with DB rollback
- `@AcceptanceTest` - Full app with web environment

### Frontend Testing

- **Do NOT write unit tests for UI components or services**
- Use Jasmine/Karma for any needed frontend tests

---

## Implementing an E2E Feature

Step-by-step guide for implementing a complete feature.

### Example: Adding a "Set Topic" Feature

**Step 1: Define API Contracts** (guessimate-lobby/api)

```java
// Command
public record SetTopicCommand(SessionId sessionId, UserId userId, String topic)
    implements UserActionCommand<SetTopicCommand> {}

// Event
public record TopicSetEvent(SessionId sessionId, String topic)
    implements LobbyEvent {}

// Update sealed interface permits clause!
```

**Step 2: Implement Domain Logic** (guessimate-lobby/application/domain)

```java
// In Lobby.java
public void setTopic(UserId userId, String topic) {
    // Business logic / invariants
    if (!state.isUserAdmin(userId)) {
        return;
    }
    record(new TopicSetEvent(sessionId(), topic));
}

// In LobbyState.java
public void apply(TopicSetEvent event) {
    this.topic = event.topic();
}
```

**Step 3: Create/Update Command Handler** (guessimate-lobby/application/usecase)

```java
// In LobbyCommandHandler.java - add case to switch
case SetTopicCommand(var topic) -> lobby.setTopic(userId, topic);
```

**Step 4: Update Read Model** (guessimate-lobby/application/drivenadapter)

```java
// In LobbyEventHandler.java
@EventHandler
public void on(TopicSetEvent event) {
    var view = lobbyViewRepository.findBySessionId(event.sessionId());
    view.apply(event);
    sendToSession(event.sessionId(), event);
}
```

**Step 5: Add WebSocket Command** (if needed)

Update `WebSocketCommand` sealed interface with new command type.

**Step 6: Write UseCase Tests**

```java
@UseCaseTest
class SetTopicCommandHandlerTest extends CommandHandlerTestBase {

    @Test
    void setsTopic() {
        given(new UserConnectedEvent(SESSION_ID, ADMIN_ID, "Admin"));

        smd.send(new SetTopicCommand(SESSION_ID, ADMIN_ID, "Sprint Planning"));

        assertThat(smd.getEvents())
            .containsExactly(new TopicSetEvent(SESSION_ID, "Sprint Planning"));
    }
}
```

**Step 7: Implement Frontend**

1. Add TypeScript types in `session.ts`
2. Update SessionStore to handle new event
3. Add UI component/controls
4. Wire up WebSocket command sending

### Feature Implementation Checklist

- [ ] Define Command in `api/command/`
- [ ] Define Event in `api/event/` + update `permits` clause
- [ ] Implement domain logic in Aggregate
- [ ] Update State object `apply()` method
- [ ] Add handler in UseCase layer
- [ ] Update read model event handler
- [ ] Write UseCase tests
- [ ] Write Integration tests (if DB involved)
- [ ] Update frontend types
- [ ] Implement frontend UI
- [ ] Manual E2E testing

---

## Build and Test Commands

### Backend (Gradle)

```bash
# Build entire project
./gradlew build

# Run application
./gradlew :guessimate-api:bootRun

# Test commands
./gradlew test                    # Unit tests only
./gradlew useCaseTest             # UseCase tests
./gradlew integrationTest         # Integration tests (TestContainers)
./gradlew acceptanceTest          # Acceptance tests
./gradlew check                   # All tests

# Module-specific
./gradlew :guessimate-lobby:application:test
./gradlew :guessimate-session:application:integrationTest
```

### Frontend (Angular)

```bash
cd guessimate-ui
npm install
npm start          # Dev server with API proxy
npm run build      # Production build
npm test           # Run tests
```

### Full Stack Development

- Backend: `http://localhost:8080`
- Frontend: `http://localhost:4200` (proxies `/api/*` to backend)

---

## Java Code Conventions

### Records for Immutable Data

```java
// Value objects, Commands, Events, DTOs
public record UserId(String value) {
    public UserId {
        requireNonNull(value, "UserId cannot be null");
    }
}
```

### Sealed Interfaces

```java
public sealed interface LobbyEvent permits
    UserConnectedEvent,
    EstimateSetEvent,
    // ... MUST update when adding new events
    EstimationCompletedEvent {}
```

### Aggregate Pattern

```java
public class Lobby {
    private final LobbyState state;
    private final List<LobbyEvent> events = new ArrayList<>();

    public void doAction(UserId userId) {
        // 1. Check invariants
        // 2. Record event
        record(new ActionEvent(sessionId(), userId));
    }

    private void record(LobbyEvent event) {
        state.apply(event);  // Update state
        events.add(event);   // Accumulate for publishing
    }

    public List<LobbyEvent> consumeEvents() {
        var copy = new ArrayList<>(events);
        events.clear();
        return copy;
    }
}
```

### Dependency Injection

Always constructor-based, all fields `final`:

```java
@Component
public class MyHandler {
    private final SomeDependency dependency;

    public MyHandler(SomeDependency dependency) {
        this.dependency = dependency;
    }
}
```

### Naming Conventions

- Commands: `<Action>Command` (e.g., `SetEstimateCommand`)
- Events: `<Action>Event` (e.g., `EstimateSetEvent`)
- Queries: `Find<Resource><Criteria>Query` (e.g., `FindSessionByIdQuery`)
- Handlers: `<Domain><Type>Handler` (e.g., `LobbyCommandHandler`)
- IDs: `<Entity>Id` (e.g., `UserId`, `SessionId`)

---

## Git Commit Best Practices

### Commit Message Format

Use this format for commit messages:

```
<type>: <short description>

[optional body with more detail]
```

**Types**:
- `feat` - New feature
- `fix` - Bug fix
- `refactor` - Code restructuring without behavior change
- `test` - Adding or updating tests
- `docs` - Documentation changes
- `style` - Formatting, whitespace (no code change)
- `chore` - Build, config, dependency updates

**Examples**:
```
feat: add auto-reveal toggle for estimation rounds

fix: prevent duplicate user connections to lobby

refactor: extract insight resolution logic to separate resolvers

test: add usecase tests for SetEstimateCommand handler
```

**Note**: Do NOT include AI attribution lines like "Generated with Claude Code" or "Co-Authored-By: Claude Sonnet" in commit messages.

### Commit Guidelines

1. **Atomic commits** - Each commit should represent one logical change. Don't mix unrelated changes.

2. **Descriptive messages** - The short description should complete the sentence: "This commit will..."
   - Good: `add reaction support to estimation cards`
   - Bad: `updated stuff` or `WIP`

3. **Present tense, imperative mood** - Write "add feature" not "added feature" or "adds feature"

4. **Keep first line under 72 characters** - Use the body for additional detail if needed

5. **Reference issues when applicable** - Include `#123` in the message to link to GitHub issues

### When to Commit

- After completing a logical unit of work
- Before switching to a different task
- After passing tests for new functionality
- After each step of a refactoring that leaves the code in a working state

### What NOT to Commit

- Generated files (build output, `node_modules/`, etc.)
- IDE/editor configuration (unless shared team settings)
- Secrets, API keys, credentials
- Files with merge conflict markers
- Broken code that doesn't compile

### Branch Naming

When creating branches, use this pattern:

```
<type>/<short-description>
```

**Examples**:
```
feat/reaction-support
fix/lobby-disconnect-handling
refactor/extract-insight-resolvers
```

---

## Important Reminders

1. **Update sealed interface permits** when adding new Commands/Events
2. **Configure processing groups** for new event handlers
3. **Write UseCase tests** for all new handlers
4. **Never persist Lobby** - it's ephemeral, only Session is persisted
5. **Use records** for all immutable data structures
6. **Follow hexagonal boundaries** - domain must not depend on frameworks
7. **Constructor injection only** - no field or setter injection
