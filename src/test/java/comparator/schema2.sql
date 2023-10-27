create table example (
    attr1 int,
    attr6 int,
    attr22 varchar(20) not null,
    attr3 varchar(20),
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

create table table_with_indices (
    attr1 int,
    attr2 int,
    attr3 int,
    attr4 int,
    primary key (attr1, attr2)
);

create index index1 on table_with_indices (attr4, attr3);

create table imported_table (
    attr1 int,
    attr2 varchar(20),
    primary key (attr1, attr2)
);

create table table_with_foreign_keys (
    attr3 int,
    attr4 varchar(20),
    foreign key (attr3, attr4) references imported_table (attr1, attr2)
);