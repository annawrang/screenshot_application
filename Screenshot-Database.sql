create database screenshot;

use screenshot;

create table Images(
ID int not null auto_increment primary key,
image longblob,
number int not null, 
name varchar(60) not null);

create index IX_Images_name on Images(name);

create index IX_Images_number on Images(number);
