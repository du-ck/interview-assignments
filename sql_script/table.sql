create table board
(
    id          INTEGER
        primary key,
    name        VARCHAR(20),
    description VARCHAR(50),
    created_at  TEXT,
    updated_at  TEXT
);

create table post
(
    id         INTEGER
        primary key,
    board_id   INTEGER,
    user_id    INTEGER,
    title      VARCHAR(20),
    content    TEXT,
    deleted    INTEGER,
    created_at TEXT,
    updated_at TEXT,
    deleted_at TEXT
);

create table user
(
    id         INTEGER
        primary key,
    email      VARCHAR(50)
        unique,
    nickname   VARCHAR(20),
    password   TEXT,
    role       VARCHAR(10),
    deleted    INTEGER,
    created_at TEXT,
    updated_at TEXT,
    deleted_at TEXT
);


