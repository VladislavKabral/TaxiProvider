create table users (
    id bigint primary key generated by default as identity,
    taxi_user_id bigint not null,
    customer_id varchar(50) not null unique,
    role varchar(20) not null
)