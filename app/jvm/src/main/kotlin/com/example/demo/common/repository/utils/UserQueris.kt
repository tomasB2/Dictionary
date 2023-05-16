package com.example.demo.common.repository.utils

object UserQueris {

    val getUserById = "select * from users where id = ?"

    val getUserByName = "select * from users where name = ?"

    val getUserByEmail = "select * from users where email = ?"

    val getUserByToken = "select * " +
        "from users join (select user_id from tokens where tokenString = ? ) " +
        "as tok on users.id = tok.user_id"

    val getListOfUsers = "select * from users limit ? offset ?"

    val createUser = "insert into users(name, password_verification, email) values(?, ?, ?)"

    val deleteUser = "delete from users where name = ?"

    val updateUser = "update users set name = ?, password_verification = ?, email = ? where id = ?"

    val createToken = "insert into tokens(user_id, tokenString, validity) values(?, ?, ?)"

    val deleteToken = "delete from tokens where tokenString = ?"

    val getToken = "select tokenString from tokens where user_id = ?"

    val checkTokenValidity = "select validity from tokens where tokenstring = ?"

    val getUserImage = "select * from images where user_id = ?"

    val putUserImage = "insert into images(user_id, data) values(?, ?)"
}
