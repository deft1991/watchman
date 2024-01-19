create table chat_settings
(
    id              CHARACTER varying(255)   NOT NULL,
    create_date     timestamp with time zone NOT NULL DEFAULT current_timestamp,
    update_date     timestamp with time zone,
    chat_id         bigint,
    chat_name       varchar(2000),
    chat_language   varchar(10),
    linkedin_enable boolean

);
ALTER TABLE chat_settings
    ADD CONSTRAINT chat_settings_pkey PRIMARY KEY (id);
