# java-explore-with-me
Template repository for ExploreWithMe project.
# Explore With Me

The first project stage implements a standalone statistics service. It records
endpoint hits and returns aggregated view counts, optionally filtered by URI or
counted by unique client IP addresses.

## Modules

- `main-service` — placeholder for the second project stage;
- `stats-service/stats-dto` — shared statistics DTOs;
- `stats-service/stats-client` — reusable HTTP client;
- `stats-service/stats-server` — Spring Boot statistics API backed by PostgreSQL.

Build the application with `mvn clean install`, then run the statistics service
and database with `docker compose up --build`. The API is available at port
`9090`; health is exposed at `/actuator/health`.

## Future feature

The optional feature will be event comments, developed later in the
`feature_comments` branch after the main service passes review. A user will be
able to add a comment to an event and edit or delete their own comment. An
administrator will be able to moderate and delete comments. The detailed API
will be designed when that stage begins; no comment entity, endpoint, or table
is included now.
