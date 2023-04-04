CREATE OR REPLACE FUNCTION scrapper.upd_link_stackoverflow(_id BIGINT,_link VARCHAR(255),
  _question_tags VARCHAR(50) [] = NULL::VARCHAR(50) [],_question_is_answered BOOLEAN = NULL::BOOLEAN,
  _question_last_activity_date TIMESTAMP WITH TIME ZONE = NULL::TIMESTAMP WITH TIME ZONE,
  _question_last_edit_date TIMESTAMP WITH TIME ZONE = NULL::TIMESTAMP WITH TIME ZONE,
  _owner_display_name VARCHAR(100) = NULL::VARCHAR(100),_owner_url VARCHAR(255) = NULL::VARCHAR(100),
  _date_update TIMESTAMP WITH TIME ZONE = NULL::TIMESTAMP WITH TIME ZONE, _id_tg_chat BIGINT)
  RETURNS VOID AS $$
BEGIN
  -- Проверка отсутствия чата телеграма
  IF NOT EXISTS (SELECT FROM scrapper.t_tg_chat tc WHERE tc.id = _id_tg_chat) THEN
    RAISE EXCEPTION 'Tg chat doesn''t exist.';
  END IF;
  -- Проверка отсутствия ссылки stackoverflow
  IF NOT EXISTS (SELECT FROM scrapper.t_link_stackoverflow ls WHERE ls.c_link = _link) THEN
    RAISE EXCEPTION 'Link stackoverflow doesn''t exist.';
  END IF;
  -- Проверка отсутствия ссылки stackoverflow у данного чата
  IF NOT EXISTS (SELECT FROM scrapper.t_tg_chat_link_stackoverflow tcls WHERE tcls.id_link_stackoverflow = _id AND tcls.id_tg_chat = _id_tg_chat) THEN
    RAISE EXCEPTION 'This tg chat hasn''t got that stackoverflow link.';
  END IF;

  -- Обновление ссылки stackoverflow
  UPDATE scrapper.t_link_stackoverflow
  SET c_link = COALESCE(_link, c_link),
      c_question_tags = COALESCE(_question_tags, c_question_tags),
      c_question_is_answered = COALESCE(_question_is_answered, c_question_is_answered),
      c_question_last_activity_date = COALESCE(_question_last_activity_date, c_question_last_activity_date),
      c_question_last_edit_date = COALESCE(_question_last_edit_date, c_question_last_edit_date),
      c_owner_account_id = COALESCE(_owner_account_id, c_owner_account_id),
      c_owner_display_name = COALESCE(_owner_display_name, c_owner_display_name),
      c_owner_url = COALESCE(_owner_url, c_owner_url),
      c_date_update = COALESCE(_date_update, c_date_update)
  WHERE id = _id;
END;
$$
  LANGUAGE plpgsql
  SECURITY DEFINER;

ALTER FUNCTION scrapper.upd_link_stackoverflow(_id BIGINT,_link VARCHAR(255),_question_tags VARCHAR(50) [],
  _question_is_answered BOOLEAN,_question_last_activity_date TIMESTAMP WITH TIME ZONE,
  _question_last_edit_date TIMESTAMP WITH TIME ZONE,_owner_display_name VARCHAR(100),_owner_url VARCHAR(255),
  _date_update TIMESTAMP WITH TIME ZONE, _id_tg_chat BIGINT) OWNER TO scrapper_owner;

GRANT ALL ON FUNCTION scrapper.upd_link_stackoverflow(_id BIGINT,_link VARCHAR(255),_question_tags VARCHAR(50) [],
  _question_is_answered BOOLEAN,_question_last_activity_date TIMESTAMP WITH TIME ZONE,
  _question_last_edit_date TIMESTAMP WITH TIME ZONE,_owner_display_name VARCHAR(100),_owner_url VARCHAR(255),
  _date_update TIMESTAMP WITH TIME ZONE, _id_tg_chat BIGINT)TO scrapper_owner;
REVOKE ALL ON FUNCTION scrapper.upd_link_stackoverflow(_id BIGINT,_link VARCHAR(255),_question_tags VARCHAR(50) [],
  _question_is_answered BOOLEAN,_question_last_activity_date TIMESTAMP WITH TIME ZONE,
  _question_last_edit_date TIMESTAMP WITH TIME ZONE,_owner_display_name VARCHAR(100),_owner_url VARCHAR(255),
  _date_update TIMESTAMP WITH TIME ZONE, _id_tg_chat BIGINT) FROM public;

COMMENT ON FUNCTION scrapper.upd_link_stackoverflow(_id BIGINT,_link VARCHAR(255),_question_tags VARCHAR(50) [],
  _question_is_answered BOOLEAN,_question_last_activity_date TIMESTAMP WITH TIME ZONE,
  _question_last_edit_date TIMESTAMP WITH TIME ZONE,_owner_display_name VARCHAR(100),_owner_url VARCHAR(255),
  _date_update TIMESTAMP WITH TIME ZONE, _id_tg_chat BIGINT) IS 'Добавление ссылки stackoverflow';
