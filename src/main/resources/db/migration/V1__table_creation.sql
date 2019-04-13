create schema if not exists devjr;

create table devjr.product
(
    sku                varchar(255) primary key,
    product_name       varchar(255),
    quantity_available integer(50),
    industry_price     decimal(50),
    discount           decimal(50)
);

create table devjr.order
(
    id         integer primary key auto_increment,
    order_date date
);

create table devjr.order_item
(
    id       integer primary key auto_increment,
    id_order integer      not null,
    sku      varchar(255) not null,
    quantity integer      not null,
    price    decimal      not null,
    foreign key (id_order) references devjr.order (id)

);

insert into devjr.order
    (id, order_date)
VALUES (1, current_date);

insert into devjr.order_item
    (id_order, sku, quantity, price)
values (1, 32060, 15, 423);

insert into devjr.order_item
(id_order, sku, quantity, price)
values (1, 31793, 15, 324);

create table devjr.files
(
    id        integer primary key auto_increment,
    file_name varchar(255) not null,
    nr        integer,
    sku       varchar(255),
    qt        integer,
    vl        decimal,
    status    varchar(255)
);