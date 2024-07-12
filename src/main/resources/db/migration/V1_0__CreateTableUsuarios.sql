create table usuarios
(
    id    UUID primary key,
    login varchar(100) not null,
    senha varchar(255) not null
);