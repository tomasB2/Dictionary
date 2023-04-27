package com.example.dictionarywithcompose.Activities.Auth.DataType // ktlint-disable filename// ktlint-disable package-name// ktlint-disable filename// ktlint-disable package-name
// ktlint-disable filename

data class AuthState(
    val authenticationMode: AuthenticationMode = AuthenticationMode.SIGN_IN,
    val email: String? = null,
    val password: String? = null,
    val passwordRequirements: List<PasswordRequirement> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
) {
    fun isPasswordValid(): Boolean {
        return password != null && passwordRequirements.containsAll(
            listOf(
                PasswordRequirement.CAPITAL_LETTER,
                PasswordRequirement.NUMBER,
                PasswordRequirement.EIGHT_CHARACTERS,
            ),
        )
    }
}
