CREATE OR REPLACE FUNCTION scrapper.get_link()
  RETURNS TABLE (id BIGINT, _link_type_name VARCHAR(50), c_link VARCHAR(255), c_link_data JSONB,
                c_date_update TIMESTAMP WITH TIME ZONE, c_id_tg_chat_array BIGINT[]) AS $$
BEGIN
  RETURN QUERY
  SELECT l.id, lt.c_name, l.c_link, l.c_date_update, l.c_date_update,
         array_agg(tcl.id_tg_chat) AS c_id_tg_chat_array
  FROM scrapper.t_link l
      LEFT JOIN scrapper.t_link_type lt ON l.id_link_type = lt.id
      LEFT JOIN scrapper.t_tg_chat_link tcl ON l.id = tcl.id_link
  GROUP BY l.id;
END;
$$
  LANGUAGE plpgsql
  SECURITY DEFINER;

ALTER FUNCTION scrapper.get_link() OWNER TO scrapper_owner;

GRANT ALL ON FUNCTION scrapper.get_link() TO scrapper_owner;
REVOKE ALL ON FUNCTION scrapper.get_link() FROM public;

COMMENT ON FUNCTION scrapper.get_link() IS 'Получение отслеживаемых ссылок';
