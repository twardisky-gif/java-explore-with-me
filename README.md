# java-explore-with-me
# Explore With Me

Explore With Me is a multi-module event-sharing application. The main service
manages users, categories, events, participation requests and compilations. A
separate statistics service records endpoint hits and supplies event view counts.

## Modules

- `main-service` — the main API backed by its own PostgreSQL database;
- `stats-service/stats-dto` — shared statistics DTOs;
- `stats-service/stats-client` — reusable HTTP client;
- `stats-service/stats-server` — Spring Boot statistics API backed by PostgreSQL.

Build the application with `mvn clean install`, then start both services and
their databases with `docker compose up --build`. The main API is available on
port `8080`, and the statistics API on port `9090`.

## Future feature

The optional feature will be event comments, developed later in the
`feature_comments` branch after the main service passes review. A user will be
able to add a comment to an event and edit or delete their own comment. An
administrator will be able to moderate and delete comments. The detailed API
will be designed when that stage begins; no comment entity, endpoint, or table
is included now.
