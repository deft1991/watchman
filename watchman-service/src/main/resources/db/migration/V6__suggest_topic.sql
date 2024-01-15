create table suggest_topic
(
    id          CHARACTER varying(255)   NOT NULL,
    create_date timestamp with time zone NOT NULL DEFAULT current_timestamp,
    update_date timestamp with time zone,
    chat_id     bigint,
    topic_name   varchar(2000)
);
ALTER TABLE suggest_topic
    ADD CONSTRAINT suggest_topic_pkey PRIMARY KEY (id);
