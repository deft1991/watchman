create table chat_user
(
    id                 CHARACTER varying(255)   NOT NULL,
    user_id            bigint,
    create_date        timestamp with time zone NOT NULL DEFAULT current_timestamp,
    update_date        timestamp with time zone,
    chat_id            bigint,
    first_name         varchar(200),
    last_name          varchar(200),
    user_name          varchar(200),
    bot                boolean,
    new_user           boolean,
    welcome_message_id int,
    invite_message     varchar,
    linkedin_url       varchar,
    rating             int
);
ALTER TABLE chat_user
    ADD CONSTRAINT chat_user_pkey PRIMARY KEY (id);

create index chat_user_user_id_chat_id_index
    on chat_user (user_id, chat_id);


create table message_dictionary
(
    id          CHARACTER varying(255)   NOT NULL,
    create_date timestamp with time zone NOT NULL DEFAULT current_timestamp,
    update_date timestamp with time zone,
    type        varchar(200),
    message     varchar(1000)
);
ALTER TABLE message_dictionary
    ADD CONSTRAINT message_dictionary_pkey PRIMARY KEY (id);

create index message_dictionary__index
    on message_dictionary (type);


INSERT INTO message_dictionary (id, type, message)
VALUES (gen_random_uuid(),
        'JOIN_GROUP_MESSAGE',
        'Welcome to the group %s! Please introduce yourself by typing #whois.');

INSERT INTO message_dictionary (id, type, message)
VALUES (gen_random_uuid(),
        'WELCOME_MESSAGE',
        'Welcome to the group %s!');
