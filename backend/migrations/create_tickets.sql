CREATE TABLE tickets (
    id BIGSERIAL PRIMARY KEY,
    flight_id BIGINT,
    passenger_id BIGINT,
    seat_number VARCHAR(5),
    price DECIMAL(10, 2),
    purchase_date TIMESTAMP DEFAULT NOW(),
    status VARCHAR(20) DEFAULT 'CONFIRMED',
    FOREIGN KEY (flight_id) REFERENCES flights(id),
    FOREIGN KEY (passenger_id) REFERENCES passengers(id),
    UNIQUE (flight_id, seat_number)
);