CREATE TABLE car
(
    id    integer primary key,
    brand text,
    model text,
    price  numeric(10, 2)
);

CREATE TABLE person
(
    id      integer primary key,
    name    text,
    age     integer,
    license boolean,
    id_car  bigint references car (id)
);