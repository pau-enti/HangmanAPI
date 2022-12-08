package com.hangman.api

/**
 * Created by sinaastani on 4/26/18.
 */
class InvalidCharacterException(s: String?) : Exception(String.format("Guessed character %s is invalid.", s))