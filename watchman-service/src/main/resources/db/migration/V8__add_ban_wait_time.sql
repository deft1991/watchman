alter table public.chat_settings
    add ban_wait_time_seconds integer default 1800 not null;
