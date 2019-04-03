create schema if not exists devjr;

create table devjr.product
(
  sku varchar(255) primary key,
  product_name varchar(255),
  quantity_available integer,
  industry_price decimal,
  discount decimal
);

create table devjr.order_item
(
  sku varchar(255) primary key,
  quantity integer,
  price decimal
);

create table devjr.order
(
  id integer primary key,
  order_date date,
  order_items varchar(255),
  foreign key (order_items) references devjr.order_item (sku)
);

/*load data infile 'C:/Users/Tainan Rodrigues/Documents/PROJETOS/devjr/product.csv'
  INTO TABLE devjr.product
  fields terminated by ';'
  lines terminated by '/n' (sku, product_name, quantity_available, industry_price, discount);*/