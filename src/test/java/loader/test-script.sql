drop database if exists dbcomparetest;
create database dbcomparetest;
use dbcomparetest;

create table example (
    attr1 int,
    attr6 int,
    attr2 varchar(20),
    PRIMARY KEY (attr1, attr6)
);

create table foreign_example (
    attr3 int,
    attr4 int,
    attr6 int,
    attr5 date,
    primary key (attr3, attr4),
    foreign key (attr4, attr6) references example (attr1, attr6)
);