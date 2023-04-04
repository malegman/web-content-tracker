CREATE TABLE IF NOT EXISTS scrapper.t_link (
    id BIGSERIAL NOT NULL,
    c_link VARCHAR(255) NOT NULL,
    c_link_data JSONB,
    c_date_create TIMESTAMP WITH TIME ZONE NOT NULL,
    c_date_update TIMESTAMP WITH TIME ZONE NOT NULL
);

ALTER TABLE scrapper.t_link ADD CONSTRAINT scrapper__t_link__id__pk PRIMARY KEY (id);
ALTER TABLE scrapper.t_link ADD CONSTRAINT scrapper__t_link__c_link__uq UNIQUE (c_link);

ALTER TABLE scrapper.t_link OWNER TO scrapper_owner;

GRANT ALL ON TABLE scrapper.t_link TO scrapper_owner;
REVOKE ALL ON TABLE scrapper.t_link FROM public;

COMMENT ON TABLE scrapper.t_link IS 'Отслеживаемая ссылка';