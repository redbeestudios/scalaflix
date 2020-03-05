-- !Ups

CREATE TABLE views (
   id       BIGSERIAL PRIMARY KEY,
   film_id  BIGINT NOT NULL,
   datetime TIMESTAMP NOT NULL
);


-- !Downs

DROP TABLE views;
