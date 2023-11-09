create table uniquetable2 (
    a int
);

create table uniquetable3 (
    b varchar(20)
);

create table differentcolumns (
    attr1 int,
    attrX int,
    attr3 numeric(10, 2),
    attr4 varchar(30),
    attr5 int
);

create table differentpks (
    attr1 int not null,
    attr2 int not null,
    attr3 varchar(10) not null,
    PRIMARY KEY (attr1, attr3)
);

create table differentindices (
    attr1 int not null,
    attr2 int not null,
    attr3 int not null,
    attr4 int not null,
    UNIQUE(attr4),
    UNIQUE(attr1, attr3)
);

create table differenttriggers (
    attr1 int not null,
    attr2 varchar(20) not null,
    attr3 float not null
);
create table importedtable (
    a int,
    b int,
    c int
);

create index idx1 on importedtable (a, b);
create index idx2 on importedtable (b, c);

create table differentfks (
    a int,
    b int,
    c int,
    foreign key (b, c) references importedtable (b, c)
);
