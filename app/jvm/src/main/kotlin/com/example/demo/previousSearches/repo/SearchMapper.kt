package com.example.demo.previousSearches.repo

import com.example.demo.meanings.domain.model.WordInfo
import com.example.demo.meanings.domain.model.wordInfoFromJson
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
