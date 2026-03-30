CREATE TABLE flight_crew (
    id BIGSERIAL PRIMARY KEY,
    flight_id BIGINT,
    employee_id BIGINT,
    role_on_flight VARCHAR(50),
    FOREIGN KEY (flight_id) REFERENCES flights(id),
    FOREIGN KEY (employee_id) REFERENCES employees(id)
);