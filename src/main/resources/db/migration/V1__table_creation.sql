create schema if not exists devjr;

create table devjr.product
(
  sku varchar(255) primary key,
  product_name varchar(255),
  quantity_available integer(50),
  industry_price decimal(50),
  discount decimal(50)
);

insert into devjr.product
(
  sku, product_name, quantity_available, industry_price, discount
)
values (
         32060,
         'NAN COMFOR 3 FORMULA INFANTIL PO 800G',
         101,
         84.18,
         57
       );

insert into devjr.product
(
  sku, product_name, quantity_available, industry_price, discount
)
values (
         31793,
         'CREATINA HARDCORE POWDER 300G',
         102,
         81.03,
         51
       );


create table devjr.order_item
(
    sku varchar(255) not null,
    quantity integer not null,
    price decimal not null,
    primary key (sku, quantity, price)
);

create table devjr.order
(
  id integer primary key,
  order_date date,
  sku_order_item varchar(255),
  foreign key (sku_order_item) references devjr.order_item(sku)

);



/*LOAD DATA INFILE 'product.csv'
  INTO TABLE devjr.product
  FIELDS TERMINATED BY ';'
  LINES TERMINATED BY '\n';*/

create table devjr.files
(
  nr integer,
  sku varchar(255),
  qt integer,
  vl decimal,
  status varchar(255)
);