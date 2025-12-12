CREATE TABLE url_mapping (
    id BIGINT NOT NULL AUTO_INCREMENT,
    long_url VARCHAR(2048) NOT NULL,
    short_key VARCHAR(10) NOT NULL,
    created_at TIMESTAMP,
    clicks INT DEFAULT 0,

    PRIMARY KEY (id),

    UNIQUE KEY uk_short_key (short_key)
);