create table uniqueTable2 (
    a int
);

create table uniqueTable3 (
    b varchar(20)
);

create table differentcolumns (
    attr1 int,
    attrX int,
    attr3 numeric(10, 2),
    attr4 varchar(30),
    attr5 int
);

create table differentPks (
    attr1 int not null,
    attr2 int not null,
    attr3 varchar(10) not null,
    PRIMARY KEY (attr1, attr3)
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
