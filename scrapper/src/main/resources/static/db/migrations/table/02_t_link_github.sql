CREATE TABLE IF NOT EXISTS scrapper.t_link_github (
    id BIGSERIAL NOT NULL,
    c_link VARCHAR(255) NOT NULL,
    c_repo_id BIGINT,
    c_repo_name VARCHAR(100) NOT NULL,
    c_repo_watchers_count INTEGER,
    c_repo_size INTEGER,
    c_owner_id BIGINT,
    c_owner_login VARCHAR(50) NOT NULL,
    c_owner_url VARCHAR(100),
    c_date_create TIMESTAMP WITH TIME ZONE NOT NULL,
    c_date_update TIMESTAMP WITH TIME ZONE NOT NULL
);

ALTER TABLE scrapper.t_link_github ADD CONSTRAINT scrapper__t_link_github__id__pk PRIMARY KEY (id);
ALTER TABLE scrapper.t_link_github ADD CONSTRAINT scrapper__t_link_github__c_link__uq UNIQUE (c_link);

ALTER TABLE scrapper.t_link_github OWNER TO scrapper_owner;

GRANT ALL ON TABLE scrapper.t_link_github TO scrapper_owner;
REVOKE ALL ON TABLE scrapper.t_link_github FROM public;

COMMENT ON TABLE scrapper.t_link_github IS 'Ссылка github';