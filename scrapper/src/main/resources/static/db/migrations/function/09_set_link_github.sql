CREATE OR REPLACE FUNCTION scrapper.set_link_github(_link VARCHAR(255),_repo_id BIGINT = NULL::BIGINT,
  _repo_name VARCHAR(100),_repo_watchers_count INTEGER = NULL::INTEGER,_repo_size INTEGER = NULL::INTEGER,
  _owner_id BIGINT = NULL::BIGINT,_owner_login VARCHAR(50),_owner_url VARCHAR(100) = NULL::VARCHAR(50),
  _date_update TIMESTAMP WITH TIME ZONE, _id_tg_chat BIGINT)
  RETURNS INTEGER AS $$
DECLARE
  _id_link_github BIGINT;
BEGIN
  -- Проверка отсутствия чата телеграма
  IF NOT EXISTS (SELECT FROM scrapper.t_tg_chat tc WHERE tc.id = _id_tg_chat) THEN
    RAISE EXCEPTION 'Tg chat doesn''t exist.';
  END IF;
  -- Проверка существования ссылки github
  IF EXISTS (SELECT FROM scrapper.t_link_github lg WHERE lg.c_link = _link) THEN
    RAISE EXCEPTION 'Link github already exists.';
  END IF;

  -- Создание ссылки github
  INSERT INTO scrapper.t_link_github (c_link, c_repo_id, c_repo_name, c_repo_watchers_count, c_repo_size,
                                      c_owner_id, c_owner_login, c_owner_url, c_date_create, c_date_update)
  VALUES (_link, _repo_id, _repo_name, _repo_watchers_count, _repo_size,
          _owner_id, _owner_login, _owner_url, now(), _date_update)
  RETURNING id INTO _id_link_github;
  -- Добавление ссылки github к телеграм чату
  INSERT INTO scrapper.t_tg_chat_link_github (id_tg_chat, id_link_github)
  VALUES (_id_tg_chat, _id_link_github);

  RETURN _id_link_github;
END;
$$
  LANGUAGE plpgsql
  SECURITY DEFINER;

ALTER FUNCTION scrapper.set_link_github(_link VARCHAR(255),_repo_id BIGINT, _repo_name VARCHAR(100),
  _repo_watchers_count INTEGER,_repo_size INTEGER,_owner_id BIGINT,_owner_login VARCHAR(50),_owner_url VARCHAR(100),
  _date_update TIMESTAMP WITH TIME ZONE, _id_tg_chat BIGINT) OWNER TO scrapper_owner;

GRANT ALL ON FUNCTION scrapper.set_link_github(_link VARCHAR(255),_repo_id BIGINT, _repo_name VARCHAR(100),
  _repo_watchers_count INTEGER,_repo_size INTEGER,_owner_id BIGINT,_owner_login VARCHAR(50),_owner_url VARCHAR(100),
  _date_update TIMESTAMP WITH TIME ZONE, _id_tg_chat BIGINT) TO scrapper_owner;
REVOKE ALL ON FUNCTION scrapper.set_link_github(_link VARCHAR(255),_repo_id BIGINT,_repo_name VARCHAR(100),
  _repo_watchers_count INTEGER,_repo_size INTEGER,_owner_id BIGINT,_owner_login VARCHAR(50),_owner_url VARCHAR(100),
  _date_update TIMESTAMP WITH TIME ZONE, _id_tg_chat BIGINT) FROM public;

COMMENT ON FUNCTION scrapper.set_link_github(_link VARCHAR(255),_repo_id BIGINT,_repo_name VARCHAR(100),
  _repo_watchers_count INTEGER,_repo_size INTEGER,_owner_id BIGINT,_owner_login VARCHAR(50),_owner_url VARCHAR(100),
  _date_update TIMESTAMP WITH TIME ZONE, _id_tg_chat BIGINT) IS 'Добавление ссылки github';
