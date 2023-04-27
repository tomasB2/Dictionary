drop table if exists searches;
drop table if exists tokens;
drop table if exists users;

create table users (
    name varchar(80) check(name is not null),
    password_verification varchar(80) not null,
    email varchar(100) check(email like '%@%') unique not null,
    primary key (name)
);

create table tokens (
     user_name varchar(80) references users(name),
     tokenString varchar(80) not null,
     validity bigint not null,
     primary key (user_name, tokenString)
);

CREATE TABLE searches (
    user_name varchar(80) references users(name),
    searches_json JSON,
    primary key (user_name)
);