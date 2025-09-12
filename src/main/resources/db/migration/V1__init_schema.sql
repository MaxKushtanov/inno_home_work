-- СЕКВЕНСЫ (как в  @SequenceGenerator, шаг 50)
create sequence if not exists user_seq start with 1 increment by 50;
create sequence if not exists product_seq start with 1 increment by 50;

-- USERS
create table if not exists users (
    id        bigint primary key,
    username  varchar(30) not null unique
);

-- PRODUCTS (новая сущность)
create table if not exists products (
    id              bigint primary key,
    account_number  varchar(32) not null unique,
    balance         numeric(19,2) not null default 0,
    type            varchar(20)  not null,             -- например: ACCOUNT/CARD
    user_id         bigint not null
);

alter table products
  add constraint fk_products_user
  foreign key (user_id) references users(id);

create index if not exists ix_products_user on products(user_id);