CREATE OR REPLACE FUNCTION scrapper.upd_link_github(_id BIGINT,_link VARCHAR(255),
  _repo_name VARCHAR(100),_repo_watchers_count INTEGER = NULL::INTEGER,_repo_size INTEGER = NULL::INTEGER,
  _owner_login VARCHAR(50),_owner_url VARCHAR(100) = NULL::VARCHAR(50),
  _date_update TIMESTAMP WITH TIME ZONE = NULL::TIMESTAMP WITH TIME ZONE,_id_tg_chat BIGINT)
  RETURNS VOID AS $$
BEGIN
  -- Проверка отсутствия чата телеграма
  IF NOT EXISTS (SELECT FROM scrapper.t_tg_chat tc WHERE tc.id = _id_tg_chat) THEN
    RAISE EXCEPTION 'Tg chat doesn''t exist.';
  END IF;
  -- Проверка отсутствия ссылки github
  IF NOT EXISTS (SELECT FROM scrapper.t_link_github lg WHERE lg.id = _id) THEN
    RAISE EXCEPTION 'Link github doesn''t exist.';
  END IF;
  -- Проверка отсутствия ссылки github у данного чата
  IF NOT EXISTS (SELECT FROM scrapper.t_tg_chat_link_github tclg WHERE tclg.id_link_github = _id AND tclg.id_tg_chat = _id_tg_chat) THEN
    RAISE EXCEPTION 'This tg chat hasn''t got that github link.';
  END IF;

  -- Обновление ссылки github
  UPDATE scrapper.t_link_github
  SET c_link = COALESCE(_link, c_link),
      c_repo_name = COALESCE(_repo_name, c_repo_name),
      c_repo_watchers_count = COALESCE(_repo_watchers_count, c_repo_watchers_count),
      c_repo_size = COALESCE(_repo_size, c_repo_size),
      c_owner_login = COALESCE(_owner_login, c_owner_login),
      c_owner_url = COALESCE(_owner_url, c_owner_url),
      c_date_update = COALESCE(_date_update, c_date_update)
  WHERE id = _id;
END;
$$
  LANGUAGE plpgsql
  SECURITY DEFINER;

ALTER FUNCTION scrapper.upd_link_github(_id BIGINT, _link VARCHAR(255),_repo_name VARCHAR(100),
  _repo_watchers_count INTEGER,_repo_size INTEGER,_owner_login VARCHAR(50),_owner_url VARCHAR(100),
  _date_update TIMESTAMP WITH TIME ZONE,_id_tg_chat BIGINT) OWNER TO scrapper_owner;

GRANT ALL ON FUNCTION scrapper.upd_link_github(_id BIGINT, _link VARCHAR(255),_repo_name VARCHAR(100),
  _repo_watchers_count INTEGER,_repo_size INTEGER,_owner_login VARCHAR(50),_owner_url VARCHAR(100),
  _date_update TIMESTAMP WITH TIME ZONE,_id_tg_chat BIGINT) TO scrapper_owner;
REVOKE ALL ON FUNCTION scrapper.upd_link_github(_id BIGINT, _link VARCHAR(255),_repo_name VARCHAR(100),
  _repo_watchers_count INTEGER,_repo_size INTEGER,_owner_login VARCHAR(50),_owner_url VARCHAR(100),
  _date_update TIMESTAMP WITH TIME ZONE,_id_tg_chat BIGINT) FROM public;

COMMENT ON FUNCTION scrapper.upd_link_github(_id BIGINT, _link VARCHAR(255),_repo_name VARCHAR(100),
  _repo_watchers_count INTEGER,_repo_size INTEGER,_owner_login VARCHAR(50),_owner_url VARCHAR(100),
  _date_update TIMESTAMP WITH TIME ZONE,_id_tg_chat BIGINT) IS 'Обновление ссылки github';
