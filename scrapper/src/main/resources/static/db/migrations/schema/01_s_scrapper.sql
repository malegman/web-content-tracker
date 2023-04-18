DROP SCHEMA IF EXISTS scrapper CASCADE;
CREATE SCHEMA scrapper AUTHORIZATION scrapper_owner;

GRANT ALL ON SCHEMA scrapper TO scrapper_owner;
REVOKE ALL ON SCHEMA scrapper FROM public;