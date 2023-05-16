package com.example.demo.meanings.domain.resolvers

import com.example.demo.meanings.domain.WordResolver
import com.example.demo.meanings.domain.model.Definition
import com.example.demo.meanings.domain.model.Meaning
import com.example.demo.meanings.domain.model.WordInfo
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.web.client.RestTemplate

class PortugueseWordResolver : WordResolver {

    override val baseUrl = "https://api.dicionario-aberto.net/word/<palavra>"

    override fun getMeaning(word: String): WordInfo? {
        val apiUrl = baseUrl.replace("<palavra>", word)
        return try {
            val restTemplate = RestTemplate()
            val apiResponse = restTemplate.getForObject(apiUrl, String::class.java)
            extractResponse(apiResponse)
        } catch (e: org.springframework.web.client.RestClientException) {
            null
        }
    }

    private fun extractResponse(response: String?): WordInfo? {
        if (response == null || response == "[]") return null
        val objectMapper = ObjectMapper()
        val rootNode = objectMapper.readTree(response)
        val meanings = rootNode.at("/0/xml").asText()
        val meaningsList = mutableListOf<Meaning>()
        val definitionsList = mutableListOf<Definition>()
        meanings.split("\n").forEach {
            if (!it.contains("<")) {
                definitionsList.add(Definition(it, null, listOf(), listOf()))
            }
        }
        meaningsList.add(Meaning(null, definitionsList))
        return WordInfo(
            lang = "pt",
            word = rootNode[0]["word"].asText(),
            phonetic = null,
            meanings = meaningsList,
        )
    }
}
