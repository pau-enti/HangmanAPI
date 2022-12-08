package com.hangman.api

data class StartedGame(
    val gameId: String,
    val word: String
) {
    constructor(game: Game) : this(game.id, game.guessedWord)
}