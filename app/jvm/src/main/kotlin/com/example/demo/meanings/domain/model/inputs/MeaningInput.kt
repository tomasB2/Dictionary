package com.example.demo.meanings.domain.model.inputs

import com.example.demo.common.utils.isValidWord

data class MeaningInput(
    val lang: String,
    val word: String,
) {
    init {
        require(lang.isNotEmpty()) { "Lang must not be empty" }
        require(word.isValidWord()) { "Word must not be empty" }
    }
}
