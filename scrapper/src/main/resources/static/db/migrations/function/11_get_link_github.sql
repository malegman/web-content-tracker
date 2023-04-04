CREATE OR REPLACE FUNCTION scrapper.get_link_github()
  RETURNS TABLE (id BIGINT,c_link VARCHAR(255),c_repo_id BIGINT,c_repo_name VARCHAR(100),
  c_repo_watchers_count INTEGER,c_repo_size INTEGER,c_owner_id BIGINT,c_owner_login VARCHAR(50),
  c_owner_url VARCHAR(100),c_date_update TIMESTAMP WITH TIME ZONE,c_id_tg_chat_array BIGINT[]) AS $$
BEGIN
  RETURN QUERY
  SELECT lg.id, lg.c_link, lg.c_repo_id, lg.c_repo_name, lg.c_repo_watchers_count,  lg.c_repo_size,
         lg.c_owner_id, lg.c_owner_login, lg.c_owner_url, lg.c_date_update,
         array_agg(tclg.id_tg_chat) AS c_id_tg_chat_array
  FROM scrapper.t_link_github lg
  LEFT JOIN scrapper.t_tg_chat_link_github tclg ON lg.id = tclg.id_link_github
  GROUP BY lg.id;
END;
$$
  LANGUAGE plpgsql
  SECURITY DEFINER;

ALTER FUNCTION scrapper.get_link_github() OWNER TO scrapper_owner;

GRANT ALL ON FUNCTION scrapper.get_link_github() TO scrapper_owner;
REVOKE ALL ON FUNCTION scrapper.get_link_github() FROM public;

COMMENT ON FUNCTION scrapper.get_link_github() IS 'Получение ссылок github';
