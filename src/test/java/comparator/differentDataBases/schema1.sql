create table uniquetable1 (
    a int
);

create table differentcolumns (
    attr1 int,
    attr2 int,
    attr3 int,
    attr4 varchar(20),
    attr5 int not null
);

create table differentpks (
    attr1 int not null,
    attr2 int not null,
    attr3 varchar(10) not null,
    PRIMARY KEY (attr1, attr2)
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
    foreign key (a, b) references importedtable (a, b)
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
