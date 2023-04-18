CREATE OR REPLACE FUNCTION scrapper.set_tg_chat(_id_tg_chat BIGINT) RETURNS BIGINT AS $$
DECLARE
    _id BIGINT;
BEGIN
    -- Добавление чата телеграма
    INSERT INTO scrapper.t_tg_chat (c_tg_chat_id, c_date_create)
    VALUES (_id_tg_chat, now())
    RETURNING scrapper.t_tg_chat.id INTO _id;

    RETURN _id;
END;
$$
    LANGUAGE plpgsql
    SECURITY DEFINER;

ALTER FUNCTION scrapper.set_tg_chat(_tg_chat_id BIGINT) OWNER TO scrapper_owner;

GRANT ALL ON FUNCTION scrapper.set_tg_chat(_tg_chat_id BIGINT) TO scrapper_owner;
REVOKE ALL ON FUNCTION scrapper.set_tg_chat(_tg_chat_id BIGINT) FROM public;

COMMENT ON FUNCTION scrapper.set_tg_chat(_tg_chat_id BIGINT) IS 'Добавление чата телеграма';
