truncate table ride_to_address;

truncate table promo_codes;
alter sequence promo_codes_id_seq restart with 1;

truncate table addresses cascade;
alter sequence addresses_id_seq restart with 1;

truncate table rides cascade;
alter sequence rides_id_seq restart with 1;