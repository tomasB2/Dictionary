package com.example.demo.common.repository.utils

object UserQueris {

    val getUserByName = "select * from users where name = ?"

    val getUserByEmail = "select * from users where email = ?"

    val getUserByToken = "select name, password_verification, email " +
        "from users join (select user_name from tokens where tokenString = ? ) " +
        "as tok on users.name = tok.user_name"

    val getListOfUsers = "select * from users limit ? offset ?"

    val createUser = "insert into users (name, password_verification, email) values (?, ?, ?)"

    val deleteUser = "delete from users where name = ?"

    val updateUser = "update users set name = ?, password_verification = ?, email = ? where name = ?"

    val createToken = "insert into tokens (user_name, tokenString, validity) values (?, ?, ?)"

    val deleteToken = "delete from tokens where tokenString = ?"

    val checkTokenValidity = "select * from tokens where tokenString = ?"

    val getUserImage = "select * from images where user_name = ?"

    val putUserImage = "insert into images (user_name, data) values (?, ?)"
}
