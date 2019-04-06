create schema if not exists devjr;

create table devjr.product
(
  sku varchar(255) primary key,
  product_name varchar(255),
  quantity_available integer,
  industry_price decimal,
  discount decimal
);


create table devjr.order
(
  id integer primary key,
  order_date date
);

create table devjr.order_item
(
  sku varchar(255) not null,
  quantity integer not null,
  price decimal not null,
  id_order integer not null,
  sku_item varchar(255) not null,
  primary key (sku),
  foreign key (id_order) references devjr.order(id),
  foreign key (sku_item) references devjr.product(sku)
);

/*LOAD DATA LOCAL INFILE
  'C:/Users/Tainan Rodrigues/Documents/PROJETOS/devjr/product.csv'
  INTO TABLE devjr.product
  FIELDS TERMINATED BY ';'
  ENCLOSED BY '"'
  LINES TERMINATED BY '\n';*/

create table devjr.files
(
  nr integer,
  sku varchar(255),
  qt integer,
  vl decimal,
  status varchar(255)
);

/*load data infile 'C:/Users/Tainan Rodrigues/Documents/PROJETOS/devjr/product.csv'
  INTO TABLE devjr.product
  fields terminated by ';'
  lines terminated by '/n' (sku, product_name, quantity_available, industry_price, discount);*/