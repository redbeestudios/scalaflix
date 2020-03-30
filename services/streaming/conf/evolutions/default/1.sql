-- !Ups

CREATE TABLE genres
(
    value varchar(50) PRIMARY KEY
);


CREATE TABLE films
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(200) NOT NULL,
    description VARCHAR(500) NOT NULL,
    duration    BIGINT       NULL,
    upload_date TIMESTAMP    NOT NULL,
    available   BOOLEAN      NOT NULL
);

create table films_x_genre
(
    film_id INTEGER REFERENCES films (id),
    genre   VARCHAR(50) REFERENCES genres (value)
);

INSERT INTO genres (value)
VALUES ('drama');

INSERT INTO genres (value)
VALUES ('thriller');

INSERT INTO genres (value)
VALUES ('action');

-- !Downs
DROP TABLE films_x_genre;
DROP TABLE genres;
DROP TABLE films;
