CREATE TABLE IF NOT EXISTS scrapper.t_tg_chat (
    id BIGSERIAL NOT NULL,
    c_tg_chat_id BIGINT NOT NULL,
    c_date_create TIMESTAMP WITH TIME ZONE NOT NULL
);

ALTER TABLE scrapper.t_tg_chat ADD CONSTRAINT scrapper__t_tg_chat__id__pk PRIMARY KEY (id);
ALTER TABLE scrapper.t_tg_chat ADD CONSTRAINT scrapper__t_tg_chat__c_tg_chat_id__uq UNIQUE (c_tg_chat_id);

ALTER TABLE scrapper.t_tg_chat OWNER TO scrapper_owner;

GRANT ALL ON TABLE scrapper.t_tg_chat TO scrapper_owner;
REVOKE ALL ON TABLE scrapper.t_tg_chat FROM public;

COMMENT ON TABLE scrapper.t_tg_chat IS 'Чат телеграма';