drop table if exists images;
drop table if exists searches;
drop table if exists tokens;
drop table if exists friends_list;
drop table if exists chat_group;
drop table if exists message;
drop table if exists users;

CREATE TABLE users (
    name varchar(80) check(name is not null),
    password_verification varchar(80) not null,
    email varchar(100) check(email like '%@%') unique not null,
    primary key (name)
);

CREATE TABLE friends_list (
    user_name varchar(80) references users(name),
    friends JSON,
    primary key (search_key)
);

CREATE TABLE tokens (
     user_name varchar(80) references users(name),
     tokenString varchar(80) not null,
     validity bigint not null,
     primary key (user_name, tokenString)
);

CREATE TABLE searches (
    user_name varchar(80) references users(name),
    search_key varchar(80) not null,
    searches_json JSON,
    primary key (search_key)
);

CREATE TABLE images (
    user_name varchar(80) references users(name),
    data bytea not null,
    primary key (user_name)
);


CREATE TABLE chat_group(
	id varchar(80) check(id is not null),
	name varchar(16) not null,
	members JSON not null,
	primary key (id)
);

CREATE TABLE message(
	id varchar(80),
	user_from varchar(80) references users(name),
	to_groupe varchar(80) references chat_group(id),
	m_content json not null,
	m_time bigint not null, // mudar para timestamp ver kotlin
	opend_by json not null,
	primary key(id)
);