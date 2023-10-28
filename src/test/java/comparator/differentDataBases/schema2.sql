create table uniqueTable2 (
    a int
);

create table uniqueTable3 (
    b varchar(20)
);

create table differentColumns (
    attr1 int,
    attrX int,
    attr3 varchar(30)
);

create table differentPks (
    attr1 int not null,
    attr2 int not null,
    attr3 varchar(10) not null,
    PRIMARY KEY (attr1, attr3)
);