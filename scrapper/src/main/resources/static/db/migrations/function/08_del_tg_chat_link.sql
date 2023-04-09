CREATE OR REPLACE FUNCTION scrapper.del_tg_chat_link(_link VARCHAR(255), _id_tg_chat BIGINT) RETURNS BIGINT AS $$
DECLARE
    _id_link BIGINT;
    _id_tg_chat_link BIGINT;
BEGIN
    -- Проверка отсутствия ссылки
    SELECT l.id INTO _id_link FROM scrapper.t_link l WHERE l.c_link = _link;
    IF _id_link IS NULL THEN
        RAISE EXCEPTION 'Link doesn''t exist.';
    END IF;

    -- Проверка отсутствия ссылки у данного чата
    IF NOT EXISTS (SELECT FROM scrapper.t_tg_chat_link tcl WHERE tcl.id_link = _id AND tcl.id_tg_chat = _id_tg_chat) THEN
        RAISE EXCEPTION 'This tg chat hasn''t got that link.';
    END IF;

    -- Удаление ссылки у чата телеграма
    DELETE FROM scrapper.t_tg_chat_link tcl WHERE tcl.id_link = _id AND tcl.id_tg_chat = _id_tg_chat
    RETURNING scrapper.t_tg_chat_link.id INTO _id_tg_chat_link;

    -- Удаление ссылки, если она не отслеживается никаким чатом телеграма
    IF NOT EXISTS (SELECT FROM scrapper.t_tg_chat_link tcl WHERE tcl.id_link = _id_link) THEN
        DELETE FROM scrapper.t_link l WHERE l.id = _id_link;
    END IF;

    RETURN _id_tg_chat_link;
END;
$$
    LANGUAGE plpgsql
    SECURITY DEFINER;

ALTER FUNCTION scrapper.del_tg_chat_link(_link VARCHAR(255), _tg_chat_id BIGINT) OWNER TO scrapper_owner;

GRANT ALL ON FUNCTION scrapper.del_tg_chat_link(_link VARCHAR(255), _tg_chat_id BIGINT) TO scrapper_owner;
REVOKE ALL ON FUNCTION scrapper.del_tg_chat_link(_link VARCHAR(255), _tg_chat_id BIGINT) FROM public;

COMMENT ON FUNCTION scrapper.del_tg_chat_link(_link VARCHAR(255), _tg_chat_id BIGINT) IS 'Удаление отслеживаемой ссылки чата телеграма';
