create table chat_message_dictionary
(
    id          CHARACTER varying(255)   not null,
    create_date timestamp with time zone not null default current_timestamp,
    update_date timestamp with time zone,
    chat_id     bigint,
    type        varchar(200),
    message     varchar(1000),
    language    varchar(20)                       default 'RUS' not null
);
ALTER TABLE chat_message_dictionary
    ADD CONSTRAINT chat_message_dictionary_pkey PRIMARY KEY (id);

create index chat_message_dictionary_chat_id_type_language_index
    on chat_message_dictionary (chat_id, type, language);
