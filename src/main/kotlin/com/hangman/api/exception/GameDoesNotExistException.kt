package com.hangman.api.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class GameDoesNotExistException(id: String?) : Exception(String.format("Game with id: %s does not exist.", id))