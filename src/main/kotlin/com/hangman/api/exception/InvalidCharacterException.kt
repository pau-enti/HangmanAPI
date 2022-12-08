package com.hangman.api.exception


class InvalidCharacterException(s: String?) : Exception(String.format("Guessed character %s is invalid.", s))