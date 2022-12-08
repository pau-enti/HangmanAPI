package com.hangman.api.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.hangman.api.web.WordsLists
import java.util.*

class Game(words: ArrayList<String>, val language: WordsLists.Language) {
    val id: String = createId()

    @get:JsonIgnore
    val word = chooseWord(words)

    var guessedWord = initGameString(word.length)
        private set

    var status: GameStatus = GameStatus.ACTIVE
        private set

    private var incorrectGuesses: Int = 0


    fun updateStatus() {
        if (word == guessedWord) {
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

    fun increaseIncorrectGuesses() {
        incorrectGuesses++
    }

    fun guessLetter(intent: Char) {
        val intentsFound = ArrayList<Int>()

        // find all indices where guessed character is located in word
        for (i in word.indices) {
            if (word[i] == intent)
                intentsFound.add(i)
        }

        val newGuessedWord = StringBuilder()

        for (i in guessedWord.indices) {

            // If it's a space that should be replaced with the letter
            if (intentsFound.contains(i))
                newGuessedWord.append(intent)

            // If we should maintain the result
            else
                newGuessedWord.append(guessedWord[i])
        }

        guessedWord = newGuessedWord.toString()
    }

    companion object {
        private const val MAX_TRIES = 7

        //choose word from word list
        private fun chooseWord(words: ArrayList<String>): String = words.random()

        private fun initGameString(word_len: Int): String {
            val w: String
            val sb = StringBuilder()
            for (i in 0 until word_len) {
                sb.append("_")
            }
            w = sb.toString()
            return w
        }

        private fun createId(): String = UUID.randomUUID().toString()
    }
}