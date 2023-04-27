package com.example.demo.repository.implementations.mappers

import com.example.demo.domain.meanings.model.WordInfo
import com.example.demo.domain.meanings.model.wordInfoFromJson
import java.sql.ResultSet

class SearchMapper {
    fun map(rs: ResultSet): WordInfo {
        return wordInfoFromJson(rs.getString("searches_json"))
    }

    fun mapMany(rs: ResultSet): List<WordInfo> {
        val res = mutableListOf<WordInfo>()
        while (rs.next()) {
            res.add(map(rs))
        }
        return res
    }
}
