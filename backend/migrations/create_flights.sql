CREATE TABLE flights (
    id BIGSERIAL PRIMARY KEY,
    flight_number VARCHAR(10) NOT NULL,
    airline_id BIGINT,
    aircraft_id BIGINT,
    departure_airport_id BIGINT,
    arrival_airport_id BIGINT,
    scheduled_departure TIMESTAMP,
    scheduled_arrival TIMESTAMP,
    actual_departure TIMESTAMP,
    actual_arrival TIMESTAMP,
    status VARCHAR(20) DEFAULT 'SCHEDULED',
    FOREIGN KEY (airline_id) REFERENCES airlines(id),
    FOREIGN KEY (aircraft_id) REFERENCES aircrafts(id),
    FOREIGN KEY (departure_airport_id) REFERENCES airports(id),
    FOREIGN KEY (arrival_airport_id) REFERENCES airports(id)
);