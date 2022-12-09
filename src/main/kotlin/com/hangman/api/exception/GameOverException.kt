package com.hangman.api.exception

import com.hangman.api.models.Game
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
class GameOverException(status: String) : RuntimeException(status) {
    constructor(status: Game.Status) : this(
        if (status == Game.Status.WON)
            "You won!"
        else
            "You lose"
    )
}