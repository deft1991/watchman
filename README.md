# Watchman Telegram Bot

## Table of Contents
1. [Acknowledgments](#acknowledgments)
2. [Contacts](#contacts)
3. [Introduction](#introduction)
   - 3.1 [Adding the Bot to Your Chat/Group](#adding-the-bot-to-your-chatgroup)
   - 3.2 [How to Use the Bot](#how-to-use-the-bot)
   - 3.3 [Commands Available Only for Group Administrators](#commands-available-only-for-group-administrators)
   - 3.4 [Commands Available For All Users](#commands-available-for-all-users)
   - 3.5 [Hashtags](#hashtags)
   - 3.6 [Message Sending via the Bot](#message-sending-via-the-bot)
4. [Technical Overview](#technical-overview)
   - 4.1 [YouTube Video](#youtube-video)
   - 4.2 [Architecture](#architecture)
   - 4.3 [Multimodule Structure](#multimodule-structure)
   - 4.4 [Watchman Service](#watchman-service)
   - 4.5 [Admin Service](#admin-service)
5. [Contributions](#contributions)
6. [Contact](#contact)

## Acknowledgments
Special thanks:
1. Kirill Kazakov for configuring the infrastructure and assisting in CI/CD. Kir, couldn't have done it without you.
2. Dmitriy Volykhin for your ideas and support
3. The best community in https://t.me/faangtalk for tests and patience

## Contacts
- Email: [deft1991@gmail.com](mailto:deft1991@gmail.com)
- Telegram: [deft_dev](https://t.me/deft_dev)

## Introduction
Tired of spammers flooding your Telegram groups with offers and get-rich-quick schemes? Enter Watchman Bot. This bot prompts users to introduce themselves with the tag #whois and offers LinkedIn link validation.

## Adding the Bot to Your Chat/Group
1. **Bot Link**: [faangTalk_watchman_bot](https://t.me/faangTalk_watchman_bot)
2. Make the bot an admin in your group and grant it read access to messages for proper functionality.

## How to Use the Bot
The bot comes with an extensive list of commands. Start with:
- [/help](#help): Displays all available commands.

### Commands Available Only For Group Administrators
- [/enable_linkedin](#enable_linkedin): Enables LinkedIn link validation.
- [/disable_linkedin](#disable_linkedin): Disables LinkedIn link validation.
- [/ban_wait_time_seconds](#ban_wait_time_seconds) `<time>`: Sets the time given to users to introduce themselves.
- [/set_language](#set_language) `<language>`: Sets the bot language to either RUS or ENG.
- [/get_available_message_types](#get_available_message_types): Displays available message types for customization.
- [/get_detailed_message](#get_detailed_message) `<TYPE>`: Retrieves the detailed message for a given type.
- [/change_detailed_message](#change_detailed_message) `<TYPE>` `<new_message>`: Changes the detailed message for a given type.

### Commands Available For All Users
- [/top](#top): Shows the top 5 active users.
- [/top_speaker](#top_speaker): Shows the top 5 speakers.
- [/top_reply_to](#top_reply_to): Shows the top 5 users who frequently reply to messages.
- [/top_reply_from](#top_reply_from): Shows the top 5 users who write the most answered messages.
- [/add_rating](#add_rating) `<@user_name>`: Adds a rating to a user.
- [/top_rating](#top_rating): Shows the top 5 users with the highest rating.

## Hashtags
The bot supports several hashtags, including:
- `#news`: Users can share messages with this hashtag for weekly digests.

## Message Sending via the Bot
For group administrators, an admin module allows creating and scheduling messages for distribution.

## Technical Overview

### YouTube Video
For a detailed explanation of the bot architecture, watch [this video](https://www.youtube.com/watch?v=9VNnEIYUZcA).

### Architecture
The bot is divided into several modules:
- admin-service
- watchman-service
- multistarted-service

![Architecture Image](images%2Farchitecture.png)

### Multimodule Structure
The application adopts a modular structure where each module can function independently. They are also combined into a unified application starter in the multistarted-service module.

### Watchman Service
This module processes messages received by the bot and is under active development.

### Admin Service
The admin module allows viewing statistics, sending messages, and fine-tuning bot settings.

## Contributions
Star us on GitHub!

![GitHub Stars Image](images%2Fstars.png)

## Contact
For questions, suggestions, and wishes:
- Email: [deft1991@gmail.com](mailto:deft1991@gmail.com)
- Telegram: [deft_dev](https://t.me/deft_dev)
