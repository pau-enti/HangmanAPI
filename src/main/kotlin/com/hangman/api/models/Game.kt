package com.hangman.api.models

import com.hangman.api.exception.GameOverException
import com.hangman.api.exception.InvalidCharacterException
import com.hangman.api.web.WordsLists
import java.util.*

class Game(words: ArrayList<String>, val language: WordsLists.Language, val maxTries: Int? = null) {
    val token: String = UUID.randomUUID().toString()

    val solution = words.random()

    var hangman = newGameOfLength(solution.length)
        private set

    var status: Status = Status.ACTIVE
        private set

    var incorrectGuesses: Int = 0
        private set

    enum class Status {
        ACTIVE, WON, LOST
    }


    private fun updateStatus() {
        if (solution == hangman) {
            status = Status.WON
        } else if (maxTries != null) {
            if (incorrectGuesses < maxTries) {
                status = Status.ACTIVE
            }
            if (incorrectGuesses >= maxTries) {
                status = Status.LOST
            }
        }
    }


    fun guessLetter(intent: String): Boolean {
        val letter = cleanUp(intent)

        // If letter not inside word
        if (!solution.contains(letter.toString())) {
            ++incorrectGuesses
            updateStatus()
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

    fun giveMeAHint(): Char {
        if (status != Status.ACTIVE)
            throw GameOverException(status)

        // Get a list with the unrevealed letters
        val unrevealedLetters = solution.filterIndexed { index, _ ->
            hangman.getOrNull(index) == '_'
        }

        return unrevealedLetters.randomOrNull() ?: throw InvalidCharacterException("No letters missing")
    }

    companion object {
        private fun newGameOfLength(len: Int): String {
            val sb = StringBuilder()
            for (i in 0 until len) {
                sb.append("_")
            }
            return sb.toString()
        }
    }
}