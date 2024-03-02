alter table passengers drop constraint passengers_email_key;
alter table passengers drop constraint passengers_phone_number_key;

drop table passengers cascade;