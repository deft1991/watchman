create table chat_news
(
    id          CHARACTER varying(255)   NOT NULL,
    create_date timestamp with time zone NOT NULL DEFAULT current_timestamp,
    update_date timestamp with time zone,
    chat_id     bigint,
    news_text   varchar(2000)
);
ALTER TABLE chat_news
    ADD CONSTRAINT chat_news_pkey PRIMARY KEY (id);
