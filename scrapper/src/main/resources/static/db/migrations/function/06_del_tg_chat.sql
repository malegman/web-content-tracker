CREATE OR REPLACE FUNCTION scrapper.del_tg_chat(_id_tg_chat BIGINT) RETURNS BIGINT AS $$
DECLARE
  _id BIGINT;
BEGIN
  -- Проверка отсутствия чата телеграма
  IF NOT EXISTS (SELECT FROM scrapper.t_tg_chat tc WHERE tc.c_tg_chat_id = _id_tg_chat) THEN
    RAISE EXCEPTION 'Tg chat doesn''t exist.';
  END IF;

  -- Добавление чата телеграма
  DELETE FROM scrapper.t_tg_chat tc WHERE tc.c_tg_chat_id = _id_tg_chat
  RETURNING tc.id INTO _id;

  RETURN _id;
END;
$$
  LANGUAGE plpgsql
  SECURITY DEFINER;

ALTER FUNCTION scrapper.del_tg_chat(_tg_chat_id BIGINT) OWNER TO scrapper_owner;

GRANT ALL ON FUNCTION scrapper.del_tg_chat(_tg_chat_id BIGINT) TO scrapper_owner;
REVOKE ALL ON FUNCTION scrapper.del_tg_chat(_tg_chat_id BIGINT) FROM public;

COMMENT ON FUNCTION scrapper.del_tg_chat(_tg_chat_id BIGINT) IS 'Удаление чата телеграма';
