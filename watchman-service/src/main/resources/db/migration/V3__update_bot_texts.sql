UPDATE public.message_dictionary
SET
    update_date = now(),
    message     = 'Привет, представься пожалуйста с тегом #whois

В приветствии напиши - как нашел чат, какие у тебя планы на собесы, куда готовишься? Расскажи про  свой опыт и ссылку на линкадин. Чем интересней о себе расскажешь тем полезней будет для тебя этот чат! 🤓

Осторожно! В чате работает строгий вахтер, который выкидывает тех, кто не написал сообщение #whois и не прислал ссылку на линкедИн'
WHERE type = 'JOIN_GROUP_MESSAGE';

UPDATE public.message_dictionary
SET
    update_date = now(),
    message     = 'Добро пожаловать, %s!'
WHERE type = 'WELCOME_MESSAGE';

UPDATE public.message_dictionary
SET
    update_date = now(),
    message     = 'Привет %s. Круто, что представился. Рад знакомству. Согласно правилам группы, тебе нужно добавить ссылку на линкедИн.

Без ссылки на linkedin ты будешь скоро забанен. Пожалуйста, поправь свое сообщение с тэгом #whois и добавь ссылку на линкедИн.'
WHERE type = 'ADD_LINKEDIN_MESSAGE';

UPDATE public.message_dictionary
SET
    update_date = now(),
    message     = 'Привет %s, не используй тэг #whois в своих сообщениях.
Ты можешь изменить свое прошлое сообщение с #whois тэгом и добавить информацию.'
WHERE type = 'DONT_USE_TAG';
