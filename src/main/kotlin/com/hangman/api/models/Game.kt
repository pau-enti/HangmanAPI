package com.hangman.api.models

import com.hangman.api.exception.InvalidCharacterException
import com.hangman.api.web.WordsLists
import java.util.*

class Game(words: ArrayList<String>, val language: WordsLists.Language) {
    val token: String = UUID.randomUUID().toString()

    val solution = words.random()

    var hangman = newGameOfLength(solution.length)
        private set

    var status: GameStatus = GameStatus.ACTIVE
        private set

    var incorrectGuesses: Int = 0
        private set


    private fun updateStatus() {
        if (solution == hangman) {
            status = GameStatus.WON
        } else {
            if (incorrectGuesses < MAX_TRIES) {
                status = GameStatus.ACTIVE
            }
            if (incorrectGuesses >= MAX_TRIES) {
                status = GameStatus.LOST
            }
        }
    }


    fun guessLetter(intent: String): Boolean {
        val letter = cleanUp(intent)

        // If letter not inside word
        if (!solution.contains(letter.toString())) {
            ++incorrectGuesses
            return false
        }

        val intentsFound = ArrayList<Int>()

        // Find all indices where guessed character is located in word
        for (i in solution.indices) {
            if (solution[i] == letter)
                intentsFound.add(i)
        }

        val newGuessedWord = StringBuilder()

        for (i in hangman.indices) {

            // If it's a space that should be replaced with the letter
            if (intentsFound.contains(i))
                newGuessedWord.append(letter)

            // If we should maintain the result
            else
                newGuessedWord.append(hangman[i])
        }

        hangman = newGuessedWord.toString()
        updateStatus()

        return true
    }


    private fun cleanUp(letter: String): Char {
        if (letter.isBlank()) throw InvalidCharacterException(letter)

        val guess = letter.lowercase(Locale.getDefault())
        return guess.first()
    }

    companion object {
        private const val MAX_TRIES = 7

        private fun newGameOfLength(len: Int): String {
            val sb = StringBuilder()
            for (i in 0 until len) {
                sb.append("_")
            }
            return sb.toString()
        }
    }
}