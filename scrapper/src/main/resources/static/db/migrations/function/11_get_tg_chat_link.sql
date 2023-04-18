CREATE OR REPLACE FUNCTION scrapper.get_tg_chat_link(_id_tg_chat BIGINT)
    RETURNS TABLE (id BIGINT, _link_type_name VARCHAR(50), c_link VARCHAR(255), c_link_data JSONB,
                   c_date_update TIMESTAMP WITH TIME ZONE, c_id_tg_chat_array BIGINT[]) AS $$
BEGIN

    -- Проверка отсутствия чата
    IF NOT EXISTS (SELECT FROM scrapper.t_tg_chat tc WHERE tc.id = _id_tg_chat) THEN
        RAISE EXCEPTION 'Tg chat doesn''t exist.';
    END IF;

    RETURN QUERY
        SELECT tcl.id, l.c_link, lt.c_name
        FROM scrapper.t_link l
                 LEFT JOIN scrapper.t_link_type lt ON l.id_link_type = lt.id
                 LEFT JOIN scrapper.t_tg_chat_link tcl ON l.id = tcl.id_link
        WHERE tcl.id_tg_chat = _id_tg_chat;
END;
$$
    LANGUAGE plpgsql
    SECURITY DEFINER;

ALTER FUNCTION scrapper.get_tg_chat_link(_id_tg_chat BIGINT) OWNER TO scrapper_owner;

GRANT ALL ON FUNCTION scrapper.get_tg_chat_link(_id_tg_chat BIGINT) TO scrapper_owner;
REVOKE ALL ON FUNCTION scrapper.get_tg_chat_link(_id_tg_chat BIGINT) FROM public;

COMMENT ON FUNCTION scrapper.get_tg_chat_link(_id_tg_chat BIGINT) IS 'Получение отслеживаемых ссылок чата';
