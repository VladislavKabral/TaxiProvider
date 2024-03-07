create table ride_to_address (
    ride_id bigint not null references rides(id) on delete cascade,
    address_id bigint not null references addresses(id) on delete cascade,
    primary key (ride_id, address_id)
)