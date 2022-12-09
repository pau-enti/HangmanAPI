package com.hangman.api.models

data class StartedGame(
    val token: String,
    val hangman: String
) {
    constructor(game: Game) : this(game.token, game.hangman)
}