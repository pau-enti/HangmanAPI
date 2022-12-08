package com.hangman.api.models

data class StartedGame(
    val gameId: String,
    val word: String
) {
    constructor(game: Game) : this(game.id, game.guessedWord)
}