package domain.meanings

import com.example.demo.domain.meanings.model.WordInfo

interface WordResolver {

    val baseUrl: String

    fun getMeaning(word: String): WordInfo?
}
