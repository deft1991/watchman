create table bot_message
(
    id                  CHARACTER varying(255)   NOT NULL,
    create_date         timestamp with time zone NOT NULL DEFAULT current_timestamp,
    update_date         timestamp with time zone,
    chat_id             bigint,
    message             varchar(2000),
    scheduled_send_time timestamp with time zone,
    send                boolean                  NOT NULL DEFAULT false

);
ALTER TABLE bot_message
    ADD CONSTRAINT bot_message_pkey PRIMARY KEY (id);
