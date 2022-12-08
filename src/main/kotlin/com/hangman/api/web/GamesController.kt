package com.hangman.api.web

import com.hangman.api.exception.GameDoesNotExistException
import com.hangman.api.exception.GameOverException
import com.hangman.api.exception.InvalidCharacterException
import com.hangman.api.models.Game
import com.hangman.api.models.GameStatus
import com.hangman.api.models.Guess
import com.hangman.api.models.StartedGame
import com.hangman.api.web.WordsLists.Language.*
import jakarta.servlet.ServletContext
import jakarta.servlet.http.HttpSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*
import kotlin.collections.ArrayList

@RestController
internal class GamesController {

    @Autowired
    private val servletContext: ServletContext? = null


    /**
     * Get list of active games in session
     */
    @GetMapping("/games")
    fun getGameList(session: HttpSession): List<Game> {
        val games = session.getAttribute("games")
        return if (games != null && games is List<*>) {
            games.filterIsInstance(Game::class.java) as MutableList
        } else { // if (games == null)
            ArrayList()
        }
    }

    /**
     * Get a game from session
     */
    @GetMapping("/game")
    @ExceptionHandler(GameDoesNotExistException::class)
    private fun getGame(id: String?, session: HttpSession): Game {
        val games = getGameList(session)

        for (game in games) {
            if (game.id == id)
                return game
        }
//        throw ResponseStatusException(HttpStatusCode.valueOf(400))
        throw GameDoesNotExistException(id)
    }

    /**
     * Create new game
     */
    @RequestMapping(value = ["/new"], method = [RequestMethod.GET])
    fun startGame(@RequestParam lang: String?, session: HttpSession): StartedGame {

        // Get list of games in session
        val games = session.getAttribute("games").let { data ->
            if (data is MutableList<*>) {
                data.filterIsInstance(Game::class.java) as MutableList
            } else { // if (games == null)
                ArrayList<Game>().also { new ->
                    session.setAttribute("games", new)
                }
            }
        }

        val languageCode = WordsLists.Language.values().find { lang == it.code } ?: EN
        val words = when (languageCode) {
            EN -> WordsLists.english
            CAT -> WordsLists.catala
        }

        val newGame = Game(words, languageCode)
        games.add(newGame)
        session.setAttribute("games", games)

        return StartedGame(newGame)
    }

    //exception handler for dealing with games that are not active
    @ExceptionHandler(GameOverException::class)
    private fun gameOver(): ResponseEntity<GameOverInfo> {
        val s = "Game is already complete"
        val error = GameOverInfo(s)
        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }

    /**
     * Make guess
     */
    @RequestMapping(
        value = ["/guess"],
        method = [RequestMethod.POST],
        headers = ["Accept=application/json"],
        consumes = ["application/json"],
        produces = ["application/json"]
    )
    @Throws(
        GameDoesNotExistException::class, InvalidCharacterException::class
    )
    fun makeGuess(@RequestBody guess: Guess, session: HttpSession): ResponseEntity<*> {
        val gameId = guess.game ?: throw GameDoesNotExistException(null)
        val letterInput = guess.guess ?: throw InvalidCharacterException(null)
        val letter = cleanUp(letterInput)

        val game = getGame(gameId, session)

        when (game.status) {
            GameStatus.ACTIVE -> {}
            GameStatus.WON -> return gameOver()
            GameStatus.LOST -> return gameOver()
        }

        if (!isLetterInsideWord(letter, game)) game.increaseIncorrectGuesses()
        else game.guessLetter(letter)

        game.updateStatus()

        return ResponseEntity(game, HttpStatus.OK)
    }


    //clean up input if more than one character/keep only first char
    private fun cleanUp(letter: String): Char {
        if (letter.isBlank()) throw InvalidCharacterException(letter)

        val guess = letter.lowercase(Locale.getDefault())
        return guess.first()
    }

    private fun isLetterInsideWord(letter: Char, game: Game): Boolean {
        val word = game.word
        val correct: Boolean
        val cs: CharSequence = letter.toString()
        //check if word contains given char
        correct = word.contains(cs)
        return correct
    }
}