create table public.chat_rss_settings
(
    id          character varying(255)   not null default gen_random_uuid()
        constraint chat_rss_settings_pk
            primary key,
    chat_id     bigint                   not null,
    rss_links   varchar[]                not null,
    match_words varchar[],
    create_date timestamp with time zone NOT NULL DEFAULT current_timestamp,
    update_date timestamp with time zone
);
