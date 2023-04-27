package domain

data class User(
    val name: String,
    val password_verification: String,
    val email: String,
) {
    init {
        require(name.isNotEmpty()) { "Name must not be empty" }
        require(password_verification.isNotEmpty()) { "Password must not be empty" }
        require(email.isNotEmpty()) { "Email must not be empty" }
    }
}
