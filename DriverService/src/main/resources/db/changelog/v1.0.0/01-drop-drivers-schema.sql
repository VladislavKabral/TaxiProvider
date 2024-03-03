alter table drivers drop constraint drivers_phone_number_key;
alter table drivers drop constraint drivers_email_key;

drop table drivers cascade;