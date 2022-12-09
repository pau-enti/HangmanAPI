package com.hangman.api.models.io

import com.hangman.api.models.Game

data class NewGameOutput(
    val token: String,
    val hangman: String,
    val language: String,
    val maxTries: Int?
) {
    constructor(game: Game) : this(game.token, game.hangman, game.language.code, game.maxTries)
}