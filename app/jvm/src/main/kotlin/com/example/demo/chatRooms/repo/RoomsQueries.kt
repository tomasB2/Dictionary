package com.example.demo.chatRooms.repo

object RoomsQueries {

    const val createChat = """
        INSERT INTO chat_group (id, description, name, members)
        VALUES (?, ?, ?, ?)
    """

    const val updateChat = """
        UPDATE chat_group
        SET description = ?, name = ?, members = ?
        WHERE id = ?
    """

    const val getChat = """
        SELECT * FROM chat_group
        WHERE id = ?
    """


}