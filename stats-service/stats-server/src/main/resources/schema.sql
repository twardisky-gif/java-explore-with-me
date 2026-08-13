CREATE TABLE IF NOT EXISTS endpoint_hits (
    id BIGSERIAL PRIMARY KEY,
    app VARCHAR(255) NOT NULL,
    uri VARCHAR(2048) NOT NULL,
    ip VARCHAR(64) NOT NULL,
    hit_timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_endpoint_hits_timestamp
    ON endpoint_hits (hit_timestamp);

CREATE INDEX IF NOT EXISTS idx_endpoint_hits_uri_timestamp
    ON endpoint_hits (uri, hit_timestamp);
