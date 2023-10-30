create table uniquetable1 (
    a int
);

create table differentColumns (
    attr1 int,
    attr2 int,
    attr3 varchar(20)
);

create table differentpks (
    attr1 int not null,
    attr2 int not null,
    attr3 varchar(10) not null,
    PRIMARY KEY (attr1, attr2)
);

create table differentindices (
    attr1 int not null,
    attr2 int not null,
    attr3 int not null,
    attr4 int not null unique,
    UNIQUE (attr1, attr2)
);

create index index1 on differentindices (attr3);

create table differenttriggers (
    attr1 int not null,
    attr2 varchar(20) not null,
    attr3 float not null
);
