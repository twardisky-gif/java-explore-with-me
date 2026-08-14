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

## Comments feature

Users can comment on published events, edit or delete their own comments, and
read comments with pagination. Administrators can delete any comment.

Pull request: [feature_comments → main](https://github.com/twardisky-gif/java-explore-with-me/pull/4)
