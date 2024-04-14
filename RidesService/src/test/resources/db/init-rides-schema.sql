insert into promo_codes(value, discount) values ('JAVA', '0.25');
insert into promo_codes(value, discount) values ('MODSEN', '0.5');
insert into promo_codes(value, discount) values ('IT', '0.15');
insert into promo_codes(value, discount) values ('SPRING', '0.2');
insert into promo_codes(value, discount) values ('KABRAL', '0.4');

insert into addresses(latitude, longitude) values ('53.922793', '27.573392');
insert into addresses(latitude, longitude) values ('53.912091', '27.594754');
insert into addresses(latitude, longitude) values ('53.858257', '27.485107');
insert into addresses(latitude, longitude) values ('53.874286', '27.674165');
insert into addresses(latitude, longitude) values ('53.925322', '27.594012');

insert into rides(cost, driver_id, passenger_id, started_at, ended_at, source_address_id, status, payment_type)
values ('13.0', '1', '1', '2024-02-28 19:32:47', '2024-02-28 20:00:43', '1', 'IN_PROGRESS', 'CASH');
insert into rides(cost, driver_id, passenger_id, started_at, ended_at, source_address_id, status, payment_type)
values ('10.0', '2', '1', '2024-02-28 10:32:50', '2024-02-28 11:00:43', '1', 'COMPLETED', 'CASH');
insert into rides(cost, driver_id, passenger_id, started_at, ended_at, source_address_id, status, payment_type)
values ('7.2', '3', '2', '2024-02-28 19:42:17', '2024-02-28 20:00:34', '2', 'COMPLETED', 'CASH');
insert into rides(cost, driver_id, passenger_id, started_at, ended_at, source_address_id, status, payment_type)
values ('5.4', '3', '1', '2024-02-28 15:23:57', '2024-02-28 16:01:23', '2', 'COMPLETED', 'CASH');
insert into rides(cost, driver_id, passenger_id, started_at, ended_at, source_address_id, status, payment_type)
values ('11.15', '2', '3', '2024-02-28 09:23:54', '2024-02-28 10:15:43', '3', 'COMPLETED', 'CASH');

insert into ride_to_address(ride_id, address_id) values ('1', '2');
insert into ride_to_address(ride_id, address_id) values ('2', '2');
insert into ride_to_address(ride_id, address_id) values ('3', '3');
insert into ride_to_address(ride_id, address_id) values ('4', '3');
insert into ride_to_address(ride_id, address_id) values ('5', '1');