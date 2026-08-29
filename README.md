# TuneGuessr

TuneGuessr is a song-guessing mini game. Each round plays a short audio snippet of a track (sourced
from the iTunes Search API) and the player has to guess the song title, with the snippet getting
progressively longer if they need more attempts. The backend is built with Java and Spring Boot, a
JavaFX client is planned as the front-facing app.

## Why this project exists

This is an open-source, personal side project. It's built both as a hobby and as a technical
challenge to sharpen my skills in Java, Spring Boot, Maven, Test-Driven Development (TDD), and REST
API design. The project is currently under **active development**, so expect the domain model, API
surface, and structure to keep evolving.

## Architecture

The backend follows **Hexagonal Architecture** (Ports & Adapters), keeping the game rules isolated
from any framework or infrastructure concern:

- **Domain model** (`domain.model`) — core entities and value objects such as `GameSession`, `Song`,
  `Player`, `Attempt`, `GuessResult`, and `RoundStatus`. This is where the game rules actually live.
- **Domain services** (`domain.service`) — application logic that orchestrates the domain model,
  including `GameService`, `GuessMatcher` (matches a player's guess against the song title) and
  `ScoringPolicy` (calculates the score based on attempts/snippet length).
- **Inbound ports** (`domain.port.in`) — use case interfaces exposed by the domain, e.g.
  `StartGameUseCase`, `SubmitGuessUseCase`, `SkipRoundUseCase`, `GiveUpUseCase`.
- **Outbound ports** (`domain.port.out`) — interfaces the domain depends on but doesn't implement,
  e.g. `SongCatalogPort` (song lookup, backed by the iTunes Search API), `PlayerRepositoryPort`, and
  `GameSessionRepositoryPort`.

Adapters (REST controllers, JPA repositories, the iTunes API client, etc.) will implement these ports
and live outside the domain package, so the core game logic never depends on Spring, JPA, or any
delivery mechanism. This makes the domain easy to unit test in isolation and keeps the door open to
swapping infrastructure (e.g. persistence, external APIs) without touching game rules.

## Tech stack

- **Java 17**
- **Spring Boot** (Web MVC, Data JPA, Bean Validation)
- **Maven** for build and dependency management
- **H2** as the (currently) embedded database
- **Lombok** to reduce boilerplate
- **JUnit** for Test-Driven Development — domain logic (`GameSession`, `GameService`, `GuessMatcher`,
  `ScoringPolicy`) is developed test-first
- **JavaFX** for the client application (planned)

## Roadmap / What's missing

The domain layer (model, services, and ports) is the most mature part of the codebase so far. The
adapters that plug into those ports don't exist yet. Rough list of what's still to be built:

- **Inbound adapters**
  - REST controllers exposing the use cases (`StartGameUseCase`, `SubmitGuessUseCase`,
    `SkipRoundUseCase`, `GiveUpUseCase`) over HTTP.
  - Request/response DTOs and validation for the REST layer.
  - Global exception handling / error responses.
- **Outbound adapters**
  - `SongCatalogPort` implementation backed by the real iTunes Search API (HTTP client, DTO mapping,
    error/timeout handling).
  - Audio snippet retrieval/streaming (playing only the first N seconds of a track's preview).
  - `PlayerRepositoryPort` and `GameSessionRepositoryPort` JPA implementations, plus entity
    mappings and DB schema/migrations for the H2 database.
- **Application wiring**
  - Spring `@Configuration`/bean setup connecting the domain services to the concrete adapters
    (the domain currently has no Spring dependency at all).
- **Player-facing concerns**
  - Player registration/identification (how a `Player` and its ID come into existence).
  - Persistent scoring/leaderboard, if desired.
- **JavaFX client**
  - Not started yet — the whole client application (UI, audio playback, calling the REST API) is
    still to be built.
- **Cross-cutting**
  - Integration tests (currently only the domain layer is covered by unit tests).

## Contributing

Pull requests are very welcome! Whether you want to fix a bug, improve the architecture, add test
coverage, or just learn from the codebase, all contributors are encouraged to get involved.
