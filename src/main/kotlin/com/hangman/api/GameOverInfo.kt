package com.hangman.api

/**
 * Created by sinaastani on 4/27/18.
 */
class GameOverInfo internal constructor(s: String) {
    var error = "error"

    init {
        error = s
    }
}