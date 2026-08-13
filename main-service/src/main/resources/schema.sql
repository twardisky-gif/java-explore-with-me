CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(254) NOT NULL UNIQUE,
    name VARCHAR(250) NOT NULL
);

CREATE TABLE IF NOT EXISTS categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS events (
    id BIGSERIAL PRIMARY KEY,
    annotation VARCHAR(2000) NOT NULL,
    category_id BIGINT NOT NULL REFERENCES categories (id),
    confirmed_requests BIGINT NOT NULL DEFAULT 0,
    created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    description VARCHAR(7000) NOT NULL,
    event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    initiator_id BIGINT NOT NULL REFERENCES users (id),
    latitude REAL NOT NULL,
    longitude REAL NOT NULL,
    paid BOOLEAN NOT NULL DEFAULT FALSE,
    participant_limit INTEGER NOT NULL DEFAULT 0,
    published_on TIMESTAMP WITHOUT TIME ZONE,
    request_moderation BOOLEAN NOT NULL DEFAULT TRUE,
    state VARCHAR(20) NOT NULL,
    title VARCHAR(120) NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_events_initiator ON events (initiator_id);
CREATE INDEX IF NOT EXISTS idx_events_category ON events (category_id);
CREATE INDEX IF NOT EXISTS idx_events_state_date ON events (state, event_date);

CREATE TABLE IF NOT EXISTS comments (
    id BIGSERIAL PRIMARY KEY,
    text VARCHAR(2000) NOT NULL,
    author_id BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    event_id BIGINT NOT NULL REFERENCES events (id) ON DELETE CASCADE,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_comments_event_created ON comments (event_id, created DESC);
CREATE INDEX IF NOT EXISTS idx_comments_author_created ON comments (author_id, created DESC);

CREATE TABLE IF NOT EXISTS participation_requests (
    id BIGSERIAL PRIMARY KEY,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    event_id BIGINT NOT NULL REFERENCES events (id) ON DELETE CASCADE,
    requester_id BIGINT NOT NULL REFERENCES users (id),
    status VARCHAR(20) NOT NULL,
    CONSTRAINT uq_request_event_requester UNIQUE (event_id, requester_id)
);

CREATE INDEX IF NOT EXISTS idx_requests_event ON participation_requests (event_id);
CREATE INDEX IF NOT EXISTS idx_requests_requester ON participation_requests (requester_id);

CREATE TABLE IF NOT EXISTS compilations (
    id BIGSERIAL PRIMARY KEY,
    pinned BOOLEAN NOT NULL DEFAULT FALSE,
    title VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS compilation_events (
    compilation_id BIGINT NOT NULL REFERENCES compilations (id) ON DELETE CASCADE,
    event_id BIGINT NOT NULL REFERENCES events (id) ON DELETE CASCADE,
    PRIMARY KEY (compilation_id, event_id)
);
