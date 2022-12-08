package com.hangman.api.models

class GuessOutput(
    val token: String,
    val hangman: String,
    val correct: Boolean
)