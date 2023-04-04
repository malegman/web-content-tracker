CREATE OR REPLACE FUNCTION scrapper.get_link_stackoverflow()
  RETURNS TABLE (id BIGINT,c_link VARCHAR(255),c_question_tags VARCHAR(50) [],
  c_question_is_answered BOOLEAN,c_question_last_activity_date TIMESTAMP WITH TIME ZONE,
  c_question_last_edit_date TIMESTAMP WITH TIME ZONE,c_owner_account_id BIGINT,
  c_owner_user_id BIGINT,c_owner_display_name VARCHAR(100),c_owner_url VARCHAR(255),
  c_date_update TIMESTAMP WITH TIME ZONE,c_id_tg_chat_array BIGINT[]) AS $$
BEGIN
  RETURN QUERY
    SELECT ls.id, ls.c_link, ls.c_question_tags, ls.c_question_is_answered, ls.c_question_last_activity_date,
           ls.c_question_last_edit_date, ls.c_owner_account_id, ls.c_owner_user_id, ls.c_owner_display_name,
           ls.c_owner_url, ls.c_date_update, array_agg(tcls.id_tg_chat) AS c_id_tg_chat_array
    FROM scrapper.t_link_stackoverflow ls
    LEFT JOIN scrapper.t_tg_chat_link_stackoverflow tcls ON ls.id = tcls.id_link_stackoverflow
    GROUP BY ls.id;
END;
$$
  LANGUAGE plpgsql
  SECURITY DEFINER;

ALTER FUNCTION scrapper.get_link_stackoverflow() OWNER TO scrapper_owner;

GRANT ALL ON FUNCTION scrapper.get_link_stackoverflow() TO scrapper_owner;
REVOKE ALL ON FUNCTION scrapper.get_link_stackoverflow() FROM public;

COMMENT ON FUNCTION scrapper.get_link_stackoverflow() IS 'Получение ссылок stackoverflow';
