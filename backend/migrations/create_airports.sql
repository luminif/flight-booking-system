CREATE TABLE airports (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    city VARCHAR(50),
    country VARCHAR(50),
    iata_code VARCHAR(3) UNIQUE
);