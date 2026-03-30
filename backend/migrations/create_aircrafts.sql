CREATE TABLE aircrafts (
    id BIGSERIAL PRIMARY KEY,
    model VARCHAR(50) NOT NULL,
    capacity INT,
    airline_id BIGINT,
    registration_number VARCHAR(20) UNIQUE,
    FOREIGN KEY (airline_id) REFERENCES airlines(id)
);