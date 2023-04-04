CREATE TABLE IF NOT EXISTS scrapper.t_tg_chat_link (
    id BIGSERIAL NOT NULL,
    id_tg_chat BIGINT NOT NULL,
    id_link BIGINT NOT NULL
);

ALTER TABLE scrapper.t_tg_chat_link ADD CONSTRAINT scrapper__t_tg_chat_link__id__pk PRIMARY KEY (id);
ALTER TABLE scrapper.t_tg_chat_link ADD CONSTRAINT scrapper__t_tg_chat_link__id_tg_chat__fk FOREIGN KEY (id_tg_chat)
    REFERENCES scrapper.t_tg_chat(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE;
ALTER TABLE scrapper.t_tg_chat_link ADD CONSTRAINT scrapper__t_tg_chat_link__id_link__fk FOREIGN KEY (id_link)
    REFERENCES scrapper.t_link(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

ALTER TABLE scrapper.t_tg_chat_link OWNER TO scrapper_owner;

GRANT ALL ON TABLE scrapper.t_tg_chat_link TO scrapper_owner;
REVOKE ALL ON TABLE scrapper.t_tg_chat_link FROM public;

COMMENT ON TABLE scrapper.t_tg_chat_link IS 'Ссылка чата телеграма';