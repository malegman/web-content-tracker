CREATE TABLE IF NOT EXISTS scrapper.t_tg_chat_link_github (
    id BIGSERIAL NOT NULL,
    id_tg_chat BIGINT NOT NULL,
    id_link_github BIGINT NOT NULL
);

ALTER TABLE scrapper.t_tg_chat_link_github ADD CONSTRAINT scrapper__t_tg_chat_link_github__id__pk PRIMARY KEY (id);
ALTER TABLE scrapper.t_tg_chat_link_github ADD CONSTRAINT scrapper__t_tg_chat_link_github__id_tg_chat__fk FOREIGN KEY (id_tg_chat)
    REFERENCES scrapper.t_tg_chat(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE;
ALTER TABLE scrapper.t_tg_chat_link_github ADD CONSTRAINT scrapper__t_tg_chat_link_github__id_link_github__fk FOREIGN KEY (id_link_github)
    REFERENCES scrapper.t_link_github(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

ALTER TABLE scrapper.t_tg_chat_link_github OWNER TO scrapper_owner;

GRANT ALL ON TABLE scrapper.t_tg_chat_link_github TO scrapper_owner;
REVOKE ALL ON TABLE scrapper.t_tg_chat_link_github FROM public;

COMMENT ON TABLE scrapper.t_tg_chat_link_github IS 'Ссылка github чата телеграма';