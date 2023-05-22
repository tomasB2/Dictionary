package com.example.demo.meanings.domain.model

import com.fasterxml.jackson.databind.ObjectMapper

data class WordInfo(
    val lang: String,
    val word: String,
    val phonetic: String?,
    val meanings: List<Meaning>,
) {
    fun toJson(): String {
        val objectMapper = ObjectMapper()
        return objectMapper.writeValueAsString(this)
    }
}

fun wordInfoFromJson(json: String): WordInfo? {
    if (json == "[]") return null
    val objectMapper = ObjectMapper()
    val rootNode = objectMapper.readTree(json)

    val meaningsNode = rootNode.at("/meanings")
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
    return WordInfo(
        lang = rootNode["lang"].asText(),
        word = rootNode["word"].asText(),
        phonetic = rootNode["phonetic"].asText(),
        meanings = meaningsList,
    )
}
