CREATE DATABASE watcher_server;

create user watcher_server with encrypted password 'watcher_server';
grant all privileges on database watcher_server to watcher_server;

--You are now connected to database "watcher_server" as user "postgres".
GRANT ALL ON SCHEMA public TO watcher_server;
