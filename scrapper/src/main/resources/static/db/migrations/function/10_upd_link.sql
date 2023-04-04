CREATE OR REPLACE FUNCTION scrapper.upd_link(_id BIGINT, _link VARCHAR(255), _link_data JSONB ,_id_tg_chat BIGINT)
  RETURNS VOID AS $$
BEGIN
  -- Проверка отсутствия ссылки у данного чата
  IF NOT EXISTS (SELECT FROM scrapper.t_tg_chat_link tcl WHERE tcl.id_link = _id AND tcl.id_tg_chat = _id_tg_chat) THEN
    RAISE EXCEPTION 'This tg chat hasn''t got that link.';
  END IF;

  -- Обновление ссылки
  UPDATE scrapper.t_link
  SET c_link = _link,
      c_link_data = _link_data,
      c_date_update = now()
  WHERE id = _id;
END;
$$
  LANGUAGE plpgsql
  SECURITY DEFINER;

ALTER FUNCTION scrapper.upd_link(_id BIGINT, _link VARCHAR(255), _link_data JSONB ,_id_tg_chat BIGINT) OWNER TO scrapper_owner;

GRANT ALL ON FUNCTION scrapper.upd_link(_id BIGINT, _link VARCHAR(255), _link_data JSONB ,_id_tg_chat BIGINT) TO scrapper_owner;
REVOKE ALL ON FUNCTION scrapper.upd_link(_id BIGINT, _link VARCHAR(255), _link_data JSONB ,_id_tg_chat BIGINT) FROM public;

COMMENT ON FUNCTION scrapper.upd_link(_id BIGINT, _link VARCHAR(255), _link_data JSONB ,_id_tg_chat BIGINT) IS 'Обновление отслеживаемой ссылки';
