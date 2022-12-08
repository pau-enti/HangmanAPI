package com.hangman.api.exception


class GameDoesNotExistException(id: String?) : Exception(String.format("Game with id: %s does not exist.", id))