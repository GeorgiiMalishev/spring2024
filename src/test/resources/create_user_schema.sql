CREATE TABLE IF NOT EXISTS "user" (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    firstname VARCHAR(255),
    lastname VARCHAR(255),
    email VARCHAR(255),
    gitHubLink VARCHAR(255),
    role VARCHAR(255)
);
