package com.hangman.api

/**
 * Created by sinaastani on 4/26/18.
 */
class GameDoesNotExistException(id: String?) : Exception(String.format("Game with id: %s does not exist.", id))