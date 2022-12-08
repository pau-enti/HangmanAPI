package com.hangman.api

/**
 * Created by sinaastani on 4/27/18.
 */
class GameOverException : Exception(String.format("Game is already complete"))