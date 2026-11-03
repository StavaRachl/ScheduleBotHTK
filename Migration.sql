BEGIN;

CREATE TABLE groups
(
    chat_id    BIGINT PRIMARY KEY,
    group_name VARCHAR(8)
);

CREATE TABLE user_info
(
    chat_id   BIGINT PRIMARY KEY,
    username  VARCHAR(255),
    firstname VARCHAR(255),
    lastname  VARCHAR(255),
    sponsor   BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_user_info_chat
        FOREIGN KEY (chat_id)
            REFERENCES groups (chat_id)
            ON DELETE CASCADE
);

CREATE TABLE settings
(
    chat_id      BIGINT PRIMARY KEY,
    dark_theme   BOOLEAN NOT NULL DEFAULT FALSE,
    notification BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_settings_chat
        FOREIGN KEY (chat_id)
            REFERENCES groups (chat_id)
            ON DELETE CASCADE
);

INSERT INTO groups (chat_id, group_name)
SELECT chat_id,
       group_name
FROM users;

INSERT INTO settings(chat_id, dark_theme)
SELECT chat_id,
       COALESCE(dark_theme, FALSE)
FROM users;

INSERT INTO user_info(chat_id)
SELECT chat_id
FROM users;

DO
$$
    DECLARE
        old_count BIGINT;
    BEGIN
        SELECT COUNT(*) INTO old_count FROM users;

        IF old_count <> (SELECT COUNT(*) FROM groups) THEN
            RAISE EXCEPTION 'Groups migration count mismatch';
        END IF;

        IF old_count <> (SELECT COUNT(*) FROM settings) THEN
            RAISE EXCEPTION 'Settings migration count mismatch';
        END IF;

        IF old_count <> (SELECT COUNT(*) FROM user_info) THEN
            RAISE EXCEPTION 'User info migration count mismatch';
        END IF;
    END
$$;

COMMIT;