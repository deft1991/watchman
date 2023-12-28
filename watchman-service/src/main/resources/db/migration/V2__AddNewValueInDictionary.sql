INSERT INTO message_dictionary (id, type, message)
VALUES (gen_random_uuid(),
        'DONT_USE_TAG',
        'Hi there %s, don''t use #whois in your messages.
Please, modify your previous message with #whois tag.
Hm, you know, you can, but you''ll be banned =)');
