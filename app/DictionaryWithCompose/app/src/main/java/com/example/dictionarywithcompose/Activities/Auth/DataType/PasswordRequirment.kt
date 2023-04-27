package com.example.dictionarywithcompose.Activities.Auth.DataType // ktlint-disable package-name

import androidx.annotation.StringRes
import com.example.dictionarywithcompose.R

enum class PasswordRequirement(
    @StringRes val label: Int,
) {
    CAPITAL_LETTER(R.string.password_requirment_uppercase),
    NUMBER(R.string.password_requirment_characters),
    EIGHT_CHARACTERS(R.string.password_requirment_digit),
}

enum class AuthenticationMode {
    SIGN_UP, SIGN_IN
}
