CREATE TABLE user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    firstname VARCHAR(255),
    lastname VARCHAR(255),
    email VARCHAR(255),
    gitHubLink VARCHAR(255),
    role VARCHAR(255)
);

CREATE TABLE review (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rating INT NOT NULL,
    text VARCHAR(255),
    sender_id BIGINT,
    receiver_id BIGINT,
    FOREIGN KEY (sender_id) REFERENCES user(id),
    FOREIGN KEY (receiver_id) REFERENCES user(id)
);
