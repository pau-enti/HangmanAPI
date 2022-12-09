package com.hangman.api.models.io

data class GuessOutput(
    val token: String,
    val hangman: String,
    val incorrectGuesses: Int,
    val correct: Boolean
)