CREATE TABLE passengers (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT UNIQUE,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    passport_number VARCHAR(20) UNIQUE NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);