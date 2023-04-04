CREATE OR REPLACE FUNCTION scrapper.set_link(_link VARCHAR(255), _link_data JSONB, _id_tg_chat BIGINT)
  RETURNS INTEGER AS $$
DECLARE
  _id_link BIGINT;
BEGIN
  -- Проверка отсутствия чата телеграма
  IF NOT EXISTS (SELECT FROM scrapper.t_tg_chat tc WHERE tc.id = _id_tg_chat) THEN
    RAISE EXCEPTION 'Tg chat doesn''t exist.';
  END IF;

  -- Проверка отсутствия ссылки
  IF NOT EXISTS (SELECT id INTO _id_link FROM scrapper.t_link l WHERE l.c_link = _link) THEN
      -- Создание ссылки
      INSERT INTO scrapper.t_link (c_link, c_link_data, c_date_create, c_date_update)
      VALUES (_link, _link_data, now(), now())
      RETURNING id INTO _id_link;
  END IF;

  IF EXISTS (SELECT FROM scrapper.t_tg_chat_link tcl WHERE tcl.id_link = _id_link AND tcl.id_tg_chat = _id_tg_chat) THEN
      RAISE EXCEPTION 'This tg chat already has that link.';
  END IF;

  -- Добавление ссылки к телеграм чату
  INSERT INTO scrapper.t_tg_chat_link (id_tg_chat, id_link)
  VALUES (_id_tg_chat, _id_link);

  RETURN _id_link;
END;
$$
  LANGUAGE plpgsql
  SECURITY DEFINER;

ALTER FUNCTION scrapper.set_link(_link VARCHAR(255), _link_data JSONB, _id_tg_chat BIGINT) OWNER TO scrapper_owner;

GRANT ALL ON FUNCTION scrapper.set_link(_link VARCHAR(255), _link_data JSONB, _id_tg_chat BIGINT) TO scrapper_owner;
REVOKE ALL ON FUNCTION scrapper.set_link(_link VARCHAR(255), _link_data JSONB, _id_tg_chat BIGINT) FROM public;

COMMENT ON FUNCTION scrapper.set_link(_link VARCHAR(255), _link_data JSONB, _id_tg_chat BIGINT) IS 'Добавление ссылки в отслеживание';
