CREATE TABLE IF NOT EXISTS scrapper.t_link_type (
    id SERIAL NOT NULL,
    c_name VARCHAR(50) NOT NULL
);

ALTER TABLE scrapper.t_link_type ADD CONSTRAINT scrapper__t_link_type__id__pk PRIMARY KEY (id);
ALTER TABLE scrapper.t_link_type ADD CONSTRAINT scrapper__t_link_type__c_name__uq UNIQUE (c_name);

ALTER TABLE scrapper.t_link_type OWNER TO scrapper_owner;

GRANT ALL ON TABLE scrapper.t_link_type TO scrapper_owner;
REVOKE ALL ON TABLE scrapper.t_link_type FROM public;

COMMENT ON TABLE scrapper.t_link_type IS 'Тип отслеживаемой ссылки. Поддерживается GITHUB и STACKOVERFLOW.';