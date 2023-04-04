CREATE OR REPLACE FUNCTION scrapper.set_link_stackoverflow(_link VARCHAR(255),_question_id BIGINT,
  _question_tags VARCHAR(50) [] = NULL::VARCHAR(50) [],_question_is_answered BOOLEAN = NULL::BOOLEAN,
  _question_last_activity_date TIMESTAMP WITH TIME ZONE = NULL::TIMESTAMP WITH TIME ZONE,
  _question_last_edit_date TIMESTAMP WITH TIME ZONE = NULL::TIMESTAMP WITH TIME ZONE,
  _owner_account_id BIGINT = NULL::BIGINT,_owner_user_id BIGINT = NULL::BIGINT,
  _owner_display_name VARCHAR(100) = NULL::VARCHAR(100),_owner_url VARCHAR(255) = NULL::VARCHAR(100),
  _date_update TIMESTAMP WITH TIME ZONE, _id_tg_chat BIGINT)
  RETURNS INTEGER AS $$
DECLARE
  _id_link_stackoverflow BIGINT;
BEGIN
  -- Проверка отсутствия чата телеграма
  IF NOT EXISTS (SELECT FROM scrapper.t_tg_chat tc WHERE tc.id = _id_tg_chat) THEN
    RAISE EXCEPTION 'Tg chat doesn''t exist.';
  END IF;
  -- Проверка существования ссылки stackoverflow
  IF EXISTS (SELECT FROM scrapper.t_link_stackoverflow ls WHERE ls.c_link = _link) THEN
    RAISE EXCEPTION 'Link stackoverflow already exists.';
  END IF;

  -- Создание ссылки stackoverflow
  INSERT INTO scrapper.t_link_stackoverflow (c_link, c_question_id, c_question_tags, c_question_is_answered, c_question_last_activity_date,
                                             c_question_last_edit_date, c_owner_account_id, c_owner_user_id, c_owner_display_name,
                                             c_owner_url, c_date_create, c_date_update)
  VALUES (_link, _question_id, _question_tags, _question_is_answered, _question_last_activity_date,
          _question_last_edit_date, _owner_account_id, _owner_user_id, _owner_display_name,
          _owner_url, now(), _date_update)
  RETURNING id INTO _id_link_stackoverflow;
  -- Добавление ссылки stackoverflow к телеграм чату
  INSERT INTO scrapper.t_tg_chat_link_stackoverflow (id_tg_chat, id_link_stackoverflow)
  VALUES (_id_tg_chat, _id_link_stackoverflow);

  RETURN _id_link_stackoverflow;
END;
$$
  LANGUAGE plpgsql
  SECURITY DEFINER;

ALTER FUNCTION scrapper.set_link_stackoverflow(_link VARCHAR(255),_question_tags VARCHAR(50) [],
  _question_is_answered BOOLEAN,_question_last_activity_date TIMESTAMP WITH TIME ZONE,
  _question_last_edit_date TIMESTAMP WITH TIME ZONE,_owner_account_id BIGINT,_owner_user_id BIGINT,
  _owner_display_name VARCHAR(100),_owner_url VARCHAR(255),_date_update TIMESTAMP WITH TIME ZONE, _id_tg_chat BIGINT)
  OWNER TO scrapper_owner;

GRANT ALL ON FUNCTION scrapper.set_link_stackoverflow(_link VARCHAR(255),_question_tags VARCHAR(50) [],
  _question_is_answered BOOLEAN,_question_last_activity_date TIMESTAMP WITH TIME ZONE,
  _question_last_edit_date TIMESTAMP WITH TIME ZONE,_owner_account_id BIGINT,_owner_user_id BIGINT,
  _owner_display_name VARCHAR(100),_owner_url VARCHAR(255),_date_update TIMESTAMP WITH TIME ZONE, _id_tg_chat BIGINT)
  TO scrapper_owner;
REVOKE ALL ON FUNCTION scrapper.set_link_stackoverflow(_link VARCHAR(255),_question_tags VARCHAR(50) [],
  _question_is_answered BOOLEAN,_question_last_activity_date TIMESTAMP WITH TIME ZONE,
  _question_last_edit_date TIMESTAMP WITH TIME ZONE,_owner_account_id BIGINT,_owner_user_id BIGINT,
  _owner_display_name VARCHAR(100),_owner_url VARCHAR(255),_date_update TIMESTAMP WITH TIME ZONE, _id_tg_chat BIGINT)
  FROM public;

COMMENT ON FUNCTION scrapper.set_link_stackoverflow(_link VARCHAR(255),_question_tags VARCHAR(50) [],
  _question_is_answered BOOLEAN,_question_last_activity_date TIMESTAMP WITH TIME ZONE,
  _question_last_edit_date TIMESTAMP WITH TIME ZONE,_owner_account_id BIGINT,_owner_user_id BIGINT,
  _owner_display_name VARCHAR(100),_owner_url VARCHAR(255),_date_update TIMESTAMP WITH TIME ZONE, _id_tg_chat BIGINT)
  IS 'Добавление ссылки stackoverflow';
