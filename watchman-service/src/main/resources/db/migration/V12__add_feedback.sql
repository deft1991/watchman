create table public.feedback
(
    id          character varying(255) not null default gen_random_uuid()
        constraint feedback_pk
            primary key,
    chat_id     bigint,
    message     varchar                not null,
    send        boolean                not null default false,
    update_date timestamp with time zone        default current_timestamp not null
);
