package com.hangman.api.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class InvalidCharacterException(s: String?) : Exception(String.format("Guessed character %s is invalid.", s))