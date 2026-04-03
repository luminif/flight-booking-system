CREATE TABLE airlines (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    country VARCHAR(50),
    iata_code VARCHAR(3) UNIQUE
);