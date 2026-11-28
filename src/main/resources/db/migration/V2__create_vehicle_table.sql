CREATE TABLE vehicle (
    vehicle_id BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    registration_number VARCHAR(32) NOT NULL UNIQUE,
    make VARCHAR(100),
    model VARCHAR(100),
    colour VARCHAR(50),
    first_seen_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    offence_count INTEGER NOT NULL DEFAULT 0 CHECK (offence_count >= 0)
);
