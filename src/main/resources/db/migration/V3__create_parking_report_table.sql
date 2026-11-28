CREATE TABLE parking_report (
    report_id BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    reporter_id INT NOT NULL REFERENCES users(id),
    vehicle_id BIGINT REFERENCES vehicle(vehicle_id),
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    reporter_latitude DOUBLE PRECISION,
    reporter_longitude DOUBLE PRECISION,
    address VARCHAR(500),
    violation_type VARCHAR(100) NOT NULL,
    description TEXT,
    reported_at TIMESTAMPTZ NOT NULL,
    submitted_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(32) NOT NULL DEFAULT 'PENDING_REVIEW'
        CHECK (status IN ('PENDING_REVIEW', 'OPEN', 'CLOSED', 'REJECTED', 'EXPIRED')),
    verification_status VARCHAR(32) NOT NULL DEFAULT 'PENDING'
        CHECK (verification_status IN ('PENDING', 'VERIFIED', 'REJECTED', 'NEEDS_MODERATOR')),
    clearance_status VARCHAR(32) NOT NULL DEFAULT 'NOT_CLEARED'
        CHECK (clearance_status IN ('NOT_CLEARED', 'CLEARANCE_PENDING', 'CLEARED', 'CLEARANCE_DISPUTED'))
);
