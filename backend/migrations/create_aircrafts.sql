CREATE TABLE aircrafts (
    id BIGSERIAL PRIMARY KEY,
    model VARCHAR(50) NOT NULL,
    capacity INT,
    airline_id BIGINT,
    registration_number VARCHAR(20) UNIQUE,
    seats_per_row INTEGER DEFAULT 6,
    seat_letters VARCHAR(20) DEFAULT 'A,B,C,D,E,F',
    FOREIGN KEY (airline_id) REFERENCES airlines(id)
);