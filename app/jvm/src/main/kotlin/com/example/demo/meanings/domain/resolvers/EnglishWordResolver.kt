package com.example.demo.meanings.domain.resolvers

import com.example.demo.meanings.domain.WordResolver
import com.example.demo.meanings.domain.model.Definition
import com.example.demo.meanings.domain.model.Meaning
import com.example.demo.meanings.domain.model.WordInfo
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.web.client.RestTemplate

class EnglishWordResolver : WordResolver {

    override val baseUrl = "https://api.dictionaryapi.dev/api/v2/entries/en/<word>"

    override fun getMeaning(word: String): WordInfo? {
        val apiUrl = baseUrl.replace("<word>", word)
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

        val meaningsNode = rootNode.at("/0/meanings")
        val meaningsList = mutableListOf<Meaning>()

        meaningsNode.forEach { meaningNode ->
            val partOfSpeech = meaningNode.at("/partOfSpeech").asText()
            val definitionsNode = meaningNode.at("/definitions")
            val definitionsList = mutableListOf<Definition>()

            definitionsNode.forEach { definitionNode ->
                val definitionText = definitionNode.at("/definition").asText()
                val example = definitionNode.at("/example")?.asText()
                val synonymsNode = definitionNode.at("/synonyms")
                val synonymsList = mutableListOf<String>()

                synonymsNode.forEach { synonymNode ->
                    val synonymText = synonymNode.asText()
                    synonymsList.add(synonymText)
                }

                val antonymsNode = definitionNode.at("/antonyms")
                val antonymsList = mutableListOf<String>()

                antonymsNode.forEach { antonymNode ->
                    val antonymText = antonymNode.asText()
                    antonymsList.add(antonymText)
                }

                val definition = Definition(
                    definition = definitionText,
                    example = example,
                    synonyms = synonymsList,
                    antonyms = antonymsList,
                )
                definitionsList.add(definition)
            }

            val meaning = Meaning(
                partOfSpeech = partOfSpeech,
                definitions = definitionsList,
            )
            meaningsList.add(meaning)
        }
        val wordInfo = WordInfo(
            lang = "en",
            word = rootNode[0]["word"].asText(),
            phonetic = rootNode[0]["phonetics"][1]["text"].asText(),
            meanings = meaningsList,
        )

        return wordInfo
    }
}
