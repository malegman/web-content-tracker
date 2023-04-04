CREATE TABLE IF NOT EXISTS scrapper.t_tg_chat_link_stackoverflow (
    id BIGSERIAL NOT NULL,
    id_tg_chat BIGINT NOT NULL,
    id_link_stackoverflow BIGINT NOT NULL
);

ALTER TABLE scrapper.t_tg_chat_link_stackoverflow ADD CONSTRAINT scrapper__t_tg_chat_link_stackoverflow__id__pk PRIMARY KEY (id);
ALTER TABLE scrapper.t_tg_chat_link_stackoverflow ADD CONSTRAINT scrapper__t_tg_chat_link_stackoverflow__id_tg_chat__fk FOREIGN KEY (id_tg_chat)
    REFERENCES scrapper.t_tg_chat(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE;
ALTER TABLE scrapper.t_tg_chat_link_stackoverflow ADD CONSTRAINT scrapper__t_tg_chat_link_stackoverflow__id_link_stack__fk FOREIGN KEY (id_link_stackoverflow)
    REFERENCES scrapper.t_link_stackoverflow(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

ALTER TABLE scrapper.t_tg_chat_link_stackoverflow OWNER TO scrapper_owner;

GRANT ALL ON TABLE scrapper.t_tg_chat_link_stackoverflow TO scrapper_owner;
REVOKE ALL ON TABLE scrapper.t_tg_chat_link_stackoverflow FROM public;

COMMENT ON TABLE scrapper.t_tg_chat_link_stackoverflow IS 'Ссылка stackoverflow чата телеграма';