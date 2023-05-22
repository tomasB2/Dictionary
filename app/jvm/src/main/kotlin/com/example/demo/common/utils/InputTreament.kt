package com.example.demo.common.utils

val SPECIAL_CHARS = listOf(
    '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '_', '+', '=', '{', '}', '[', ']', '|', '\\', ':', ';', '"', '\'', '<', '>', ',', '.', '?', '/', '`', '~', ' ',
)

val NUMBERS = listOf(
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
)

val LOWER_CASE = listOf(
    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
)

val UPPER_CASE = listOf(
    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
)

fun String.isValidName(): Boolean {
    if (this.isNotEmpty() && this.length >= 5 && this.length <= 16) {
        for (i in this.indices) {
            if (this[i] in SPECIAL_CHARS) {
                throw IllegalArgumentException("Name must be contain 5-16 characters and no special characters")
            }
        }
        return true
    } else {
        throw IllegalArgumentException("Name must be contain 5-16 characters and no special characters")
    }
}

fun String.isValidPassword(): Boolean {
    if (this.isNotEmpty() && this.length >= 8 && this.length <= 16) {
        if (this.contains(Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,16}\$"))) {
            return true
        } else {
            throw IllegalArgumentException("Password must be contain 8-16 characters, at least one uppercase letter, one lowercase letter ane one number")
        }
    } else {
        throw IllegalArgumentException("Password must be contain 8-16 characters, at least one uppercase letter, one lowercase letter ane one number")
    }
}

fun String.isValidEmail(): Boolean {
    if (this.isNotEmpty() && this.length >= 5 && this.length <= 30) {
        if (this.contains(Regex("^[A-Za-z0-9+_.-]+@(.+)\$"))) {
            return true
        } else {
            throw IllegalArgumentException("Email must be contain 5-30 characters and must be in email format")
        }
    } else {
        throw IllegalArgumentException("Email must be contain 5-30 characters and must be in email format")
    }
}

fun String.isValidWord(): Boolean {
    if (this.isNotEmpty() && this.length <= 30) {
        this.forEach {
            if (it in SPECIAL_CHARS || it in NUMBERS || it in UPPER_CASE) {
                throw IllegalArgumentException("Word must be contain only lowercase letters")
            }
        }
        return true
    } else {
        throw IllegalArgumentException("Word must be contain 1-30 characters")
    }
}

fun String.isValidTranslationText(): Boolean {
    if (this.isNotEmpty() && this.length <= 500) {
        this.forEach {
            if (it in SPECIAL_CHARS || it in NUMBERS) {
                throw IllegalArgumentException("Text must be contain only letters")
            }
        }
        return true
    } else {
        throw IllegalArgumentException("Text must be contain 1-500 characters")
    }
}
