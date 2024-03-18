CREATE TABLE post (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    text VARCHAR(255)
);

CREATE TABLE post_team_role_tags (
    post_id BIGINT,
    team_role_tag VARCHAR(255),
    PRIMARY KEY (post_id, team_role_tag),
    FOREIGN KEY (post_id) REFERENCES post(id)
);
