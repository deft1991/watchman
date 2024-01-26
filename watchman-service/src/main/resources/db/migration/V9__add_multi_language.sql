alter table public.message_dictionary
    add language varchar(20) default 'RUS' not null;

create unique index message_dictionary__type_language_index
    on public.message_dictionary (type, language);

INSERT INTO public.message_dictionary (id, type, message, language)
VALUES (gen_random_uuid(), 'JOIN_GROUP_MESSAGE', 'Hello %s (@%s), please introduce yourself with the tag #whois

In your greeting, write how you found the chat, what are your plans for interviews, where are you preparing? Tell us about your experience and link to LinkedIn. The more interesting you tell us about yourself, the more useful this chat will be for you! 🤓

Carefully! There is a strict guard in the chat who kicks out those who did not write a #whois message and did not send a link to LinkedIn',
        'ENG');
INSERT INTO public.message_dictionary (id, type, message, language)
VALUES (gen_random_uuid(),
        'JOIN_GROUP_MESSAGE_WITHOUT_LINKEDIN', 'Hello %s (@%s), please introduce yourself with the tag #whois

In your greeting, write how you found the chat, what are your plans for interviews, where are you preparing? Tell us about your experience. The more interesting you tell us about yourself, the more useful this chat will be for you! 🤓

Carefully! There is a strict guard in the chat who kicks out those who did not write a message #whois',
        'ENG');
INSERT INTO public.message_dictionary (id, type, message, language)
VALUES (gen_random_uuid(), 'WELCOME_MESSAGE', 'Welcome, %s (@%s)!',
        'ENG');
INSERT INTO public.message_dictionary (id, type, message, language)
VALUES (gen_random_uuid(), 'ADD_LINKEDIN_MESSAGE', 'Hello %s. It''s cool that you introduced yourself. Nice to meet you. According to the group rules, you need to add a link to LinkedIn.

Without a linkedin link you will soon be banned. Please edit your post with #whois and add a link to LinkedIn.',
        'ENG');
INSERT INTO public.message_dictionary (id, type, message, language)
VALUES (gen_random_uuid(), 'DONT_USE_TAG', 'Hey %s, don''t use the #whois tag in your posts.
You can edit your previous post with #whois tag and add information.',
        'ENG');
