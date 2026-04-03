INSERT INTO roles (name) VALUES ('ROLE_PASSENGER'), ('ROLE_EMPLOYEE'), ('ROLE_ADMIN');

INSERT INTO users (username, password, created_at) VALUES
    ('ivan.petrov', '$2a$12$aGJ71IuFHOQgML2ULNdTrOJBvj/pn6VTAdZKNnCbzirc5fE80apc6', NOW()),
    ('maria.sidorova', '$2a$12$aGJ71IuFHOQgML2ULNdTrOJBvj/pn6VTAdZKNnCbzirc5fE80apc6', NOW()),
    ('alex.smirnov', '$2a$12$aGJ71IuFHOQgML2ULNdTrOJBvj/pn6VTAdZKNnCbzirc5fE80apc6', NOW()),
    ('employee.john', '$2a$12$aGJ71IuFHOQgML2ULNdTrOJBvj/pn6VTAdZKNnCbzirc5fE80apc6', NOW()),
    ('admin.alex', '$2a$12$aGJ71IuFHOQgML2ULNdTrOJBvj/pn6VTAdZKNnCbzirc5fE80apc6', NOW());

INSERT INTO user_roles (user_id, role_id) VALUES
    ((SELECT id FROM users WHERE username = 'ivan.petrov'), (SELECT id FROM roles WHERE name = 'ROLE_PASSENGER')),
    ((SELECT id FROM users WHERE username = 'maria.sidorova'), (SELECT id FROM roles WHERE name = 'ROLE_PASSENGER')),
    ((SELECT id FROM users WHERE username = 'alex.smirnov'), (SELECT id FROM roles WHERE name = 'ROLE_PASSENGER')),
    ((SELECT id FROM users WHERE username = 'employee.john'), (SELECT id FROM roles WHERE name = 'ROLE_EMPLOYEE')),
    ((SELECT id FROM users WHERE username = 'admin.alex'), (SELECT id FROM roles WHERE name = 'ROLE_ADMIN'));

INSERT INTO passengers (user_id, first_name, last_name, passport_number, phone, email) VALUES
    ((SELECT id FROM users WHERE username = 'ivan.petrov'), 'Иван', 'Петров', 'AB1234567', '+7-912-345-67-89', 'ivan.petrov@example.com'),
    ((SELECT id FROM users WHERE username = 'maria.sidorova'), 'Мария', 'Сидорова', 'CD2345678', '+7-923-456-78-90', 'maria.sidorova@example.com'),
    ((SELECT id FROM users WHERE username = 'alex.smirnov'), 'Алексей', 'Смирнов', 'EF3456789', '+7-934-567-89-01', 'alex.smirnov@example.com');

INSERT INTO employees (user_id, first_name, last_name, position, license_number) VALUES
    ((SELECT id FROM users WHERE username = 'employee.john'), 'Джон', 'Смит', 'Пилот', 'PIL-12345');

INSERT INTO airlines (name, country, iata_code) VALUES
    ('Аэрофлот', 'Россия', 'SU'),
    ('S7 Airlines', 'Россия', 'S7'),
    ('Utair', 'Россия', 'UT'),
    ('Emirates', 'ОАЭ', 'EK'),
    ('Turkish Airlines', 'Турция', 'TK');

INSERT INTO airports (name, city, country, iata_code) VALUES
    ('Шереметьево', 'Москва', 'Россия', 'SVO'),
    ('Домодедово', 'Москва', 'Россия', 'DME'),
    ('Внуково', 'Москва', 'Россия', 'VKO'),
    ('Пулково', 'Санкт-Петербург', 'Россия', 'LED'),
    ('Сочи', 'Сочи', 'Россия', 'AER'),
    ('Кольцово', 'Екатеринбург', 'Россия', 'SVX'),
    ('Толмачёво', 'Новосибирск', 'Россия', 'OVB'),
    ('Дубай', 'Дубай', 'ОАЭ', 'DXB'),
    ('Стамбул', 'Стамбул', 'Турция', 'IST');

INSERT INTO aircrafts (model, capacity, airline_id, registration_number) VALUES
    ('Boeing 737-800', 189, (SELECT id FROM airlines WHERE iata_code = 'SU'), 'RA-73000'),
    ('Airbus A320neo', 180, (SELECT id FROM airlines WHERE iata_code = 'S7'), 'RA-73100'),
    ('Boeing 777-300ER', 402, (SELECT id FROM airlines WHERE iata_code = 'EK'), 'A6-EPH'),
    ('Airbus A330-300', 300, (SELECT id FROM airlines WHERE iata_code = 'TK'), 'TC-JNJ'),
    ('Boeing 737-800', 189, (SELECT id FROM airlines WHERE iata_code = 'UT'), 'RA-73200');

INSERT INTO flights (
    flight_number, airline_id, aircraft_id,
    departure_airport_id, arrival_airport_id,
    scheduled_departure, scheduled_arrival,
    status, price
) VALUES
    ('SU1234',
        (SELECT id FROM airlines WHERE iata_code = 'SU'),
        (SELECT id FROM aircrafts WHERE registration_number = 'RA-73000'),
        (SELECT id FROM airports WHERE iata_code = 'SVO'),
        (SELECT id FROM airports WHERE iata_code = 'LED'),
        NOW() + INTERVAL '2 hours',
        NOW() + INTERVAL '4 hours',
        'SCHEDULED',
        7500.00),

    ('S74321',
        (SELECT id FROM airlines WHERE iata_code = 'S7'),
        (SELECT id FROM aircrafts WHERE registration_number = 'RA-73100'),
        (SELECT id FROM airports WHERE iata_code = 'DME'),
        (SELECT id FROM airports WHERE iata_code = 'AER'),
        NOW() + INTERVAL '5 hours',
        NOW() + INTERVAL '8 hours',
        'SCHEDULED',
        9500.00),

    ('SU5678',
        (SELECT id FROM airlines WHERE iata_code = 'SU'),
        (SELECT id FROM aircrafts WHERE registration_number = 'RA-73000'),
        (SELECT id FROM airports WHERE iata_code = 'SVO'),
        (SELECT id FROM airports WHERE iata_code = 'AER'),
        NOW() + INTERVAL '1 day' + INTERVAL '2 hours',
        NOW() + INTERVAL '1 day' + INTERVAL '5 hours',
        'SCHEDULED',
        8500.00),

    ('EK123',
        (SELECT id FROM airlines WHERE iata_code = 'EK'),
        (SELECT id FROM aircrafts WHERE registration_number = 'A6-EPH'),
        (SELECT id FROM airports WHERE iata_code = 'DME'),
        (SELECT id FROM airports WHERE iata_code = 'DXB'),
        NOW() + INTERVAL '2 days' + INTERVAL '3 hours',
        NOW() + INTERVAL '2 days' + INTERVAL '9 hours',
        'SCHEDULED',
        45000.00),

    ('TK789',
        (SELECT id FROM airlines WHERE iata_code = 'TK'),
        (SELECT id FROM aircrafts WHERE registration_number = 'TC-JNJ'),
        (SELECT id FROM airports WHERE iata_code = 'VKO'),
        (SELECT id FROM airports WHERE iata_code = 'IST'),
        NOW() + INTERVAL '3 days' + INTERVAL '1 hour',
        NOW() + INTERVAL '3 days' + INTERVAL '6 hours',
        'SCHEDULED',
        25000.00),

    ('UT456',
        (SELECT id FROM airlines WHERE iata_code = 'UT'),
        (SELECT id FROM aircrafts WHERE registration_number = 'RA-73200'),
        (SELECT id FROM airports WHERE iata_code = 'SVX'),
        (SELECT id FROM airports WHERE iata_code = 'OVB'),
        NOW() + INTERVAL '3 hours',
        NOW() + INTERVAL '5 hours',
        'DELAYED',
        6000.00);

INSERT INTO tickets (flight_id, passenger_id, seat_number, price, purchase_date, status) VALUES
    (
        (SELECT id FROM flights WHERE flight_number = 'SU1234'),
        (SELECT id FROM passengers WHERE passport_number = 'AB1234567'),
        '12A',
        7500.00,
        NOW() - INTERVAL '2 days',
        'CONFIRMED'
    ),
    (
        (SELECT id FROM flights WHERE flight_number = 'SU1234'),
        (SELECT id FROM passengers WHERE passport_number = 'CD2345678'),
        '12B',
        7500.00,
        NOW() - INTERVAL '1 day',
        'CONFIRMED'
    ),
    (
        (SELECT id FROM flights WHERE flight_number = 'S74321'),
        (SELECT id FROM passengers WHERE passport_number = 'EF3456789'),
        '15C',
        9500.00,
        NOW() - INTERVAL '3 days',
        'CONFIRMED'
    ),
    (
        (SELECT id FROM flights WHERE flight_number = 'S74321'),
        (SELECT id FROM passengers WHERE passport_number = 'AB1234567'),
        '15D',
        9500.00,
        NOW() - INTERVAL '2 days',
        'CHECKED_IN'
    ),
    (
        (SELECT id FROM flights WHERE flight_number = 'SU5678'),
        (SELECT id FROM passengers WHERE passport_number = 'CD2345678'),
        '20A',
        8500.00,
        NOW() - INTERVAL '5 days',
        'CANCELLED'
    );

INSERT INTO flight_crew (flight_id, employee_id, role_on_flight) VALUES
    (
        (SELECT id FROM flights WHERE flight_number = 'SU1234'),
        (SELECT id FROM employees WHERE license_number = 'PIL-12345'),
        'CAPTAIN'
    ),
    (
        (SELECT id FROM flights WHERE flight_number = 'S74321'),
        (SELECT id FROM employees WHERE license_number = 'PIL-12345'),
        'CAPTAIN'
    );