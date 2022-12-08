package com.hangman.api

import jakarta.annotation.PostConstruct
import jakarta.servlet.ServletContext
import jakarta.servlet.http.HttpSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.collections.ArrayList

@RestController
internal class GamesController {
    private lateinit var wordList: ArrayList<String>

    @Autowired
    private val servletContext: ServletContext? = null

    @PostConstruct
    fun init() {
        val game = GamesInit()
        wordList = game.wordList
    }


    @GetMapping("/games")
    fun getGameList(session: HttpSession): List<Game> {
        val games = session.getAttribute("games")
        return if (games != null && games is List<*>) {
            games.filterIsInstance(Game::class.java) as MutableList
        } else { // if (games == null)
            ArrayList()
        }
    }


    @RequestMapping(value = ["/new"], method = [RequestMethod.GET])
    fun startGame(session: HttpSession): StartedGame {

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

        val newGame = Game(wordList)
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

    //POST
    //make guess
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
            else -> throw GameDoesNotExistException("no status")
        }

        if (!isLetterInsideWord(letter, game)) game.increaseIncorrectGuesses()
        else game.guessLetter(letter)

        game.updateStatus()

        return ResponseEntity(game, HttpStatus.OK)
    }

    // Find an existing game
    @Throws(GameDoesNotExistException::class)
    private fun getGame(id: String, session: HttpSession): Game {
        val games = session.getAttribute("games") as List<Game>
        var g: Game? = null
        for (i in games.indices) {
            g = games[i]
            if (g.id == id) {
                break
            }
        }
        if (g == null) {
            throw GameDoesNotExistException(id)
        }
        return g
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