-- !Ups

CREATE TABLE genres (
    value       varchar(50) PRIMARY KEY
);


CREATE TABLE films (
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(200) NOT NULL,
    description VARCHAR(500) NOT NULL,
    duration    INTEGER NULL,
    uploadDate  TIMESTAMP NOT NULL,
    views       BIGINT NOT NULL,
    available   BOOLEAN NOT NULL
);

create table filmGenre (
  filmId        INTEGER REFERENCES films(id),
  genre         VARCHAR(50) REFERENCES genres(value)
);

INSERT INTO genres (value) VALUES
    ('drama');

INSERT INTO genres (value) VALUES
    ('thriller');

INSERT INTO genres (value) VALUES
    ('action');

-- !Downs
DROP TABLE filmGenre;
DROP TABLE genres;
DROP TABLE films;
