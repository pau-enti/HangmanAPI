package com.hangman.api.exception


class GameOverException : Exception(String.format("Game is already complete"))