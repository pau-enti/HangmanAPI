package com.hangman.api.models.io

data class GuessInput(
    var token: String? = null,
    var letter: String? = null
)

