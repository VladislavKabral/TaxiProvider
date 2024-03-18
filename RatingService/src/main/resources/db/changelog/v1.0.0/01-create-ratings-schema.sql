create table ratings (
    id bigint primary key generated by default as identity,
    taxi_user_id bigint not null,
    role varchar(20) not null,
    value int not null check (value > 0) check (value <= 5),
    created_at timestamp not null
)