create table public.chat_rss_advert
(
    id             character varying(255)   not null default gen_random_uuid()
        constraint chat_rss_advert_pk
            primary key,
    chat_id        bigint                   not null,
    title          varchar                  not null,
    description    varchar                  not null,
    link           varchar                  not null,
    published      boolean                  not null default false,
    scheduled_date timestamp with time zone,
    create_date    timestamp with time zone NOT NULL DEFAULT current_timestamp,
    update_date    timestamp with time zone
);
