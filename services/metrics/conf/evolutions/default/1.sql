-- !Ups

CREATE TABLE views (
   id       SERIAL PRIMARY KEY,
   filmId   BIGINT NOT NULL,
   date     TIMESTAMP NOT NULL
);

-- !Downs
DROP TABLE views;
