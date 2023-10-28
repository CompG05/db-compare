create table uniqueTable1 (
    a int
);

create table differentColumns (
    attr1 int,
    attr2 int,
    attr3 varchar(20)
);

create table differentPks (
    attr1 int not null,
    attr2 int not null,
    attr3 varchar(10) not null,
    PRIMARY KEY (attr1, attr2)
);
