CREATE TABLE IF NOT EXISTS scrapper.t_link_stackoverflow (
    id BIGSERIAL NOT NULL,
    c_link VARCHAR(255) NOT NULL,
    c_question_id BIGINT NOT NULL,
    c_question_tags VARCHAR(50) [],
    c_question_is_answered BOOLEAN,
    c_question_last_activity_date TIMESTAMP WITH TIME ZONE,
    c_question_last_edit_date TIMESTAMP WITH TIME ZONE,
    c_owner_account_id BIGINT,
    c_owner_user_id BIGINT,
    c_owner_display_name VARCHAR(100),
    c_owner_url VARCHAR(255),
    c_date_create TIMESTAMP WITH TIME ZONE NOT NULL,
    c_date_update TIMESTAMP WITH TIME ZONE NOT NULL
);

ALTER TABLE scrapper.t_link_stackoverflow ADD CONSTRAINT scrapper__t_link_stackoverflow__id__pk PRIMARY KEY (id);
ALTER TABLE scrapper.t_link_stackoverflow ADD CONSTRAINT scrapper__t_link_stackoverflow__c_link__uq UNIQUE (c_link);

ALTER TABLE scrapper.t_link_stackoverflow OWNER TO scrapper_owner;

GRANT ALL ON TABLE scrapper.t_link_stackoverflow TO scrapper_owner;
REVOKE ALL ON TABLE scrapper.t_link_stackoverflow FROM public;

COMMENT ON TABLE scrapper.t_link_stackoverflow IS 'Ссылка stackoverflow';
